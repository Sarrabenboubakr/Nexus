package tn.esprit.autoloc.auditservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.auditservice.dto.AuditEventRequest;
import tn.esprit.autoloc.auditservice.dto.IntegrityReport;
import tn.esprit.autoloc.auditservice.entities.AuditEvent;
import tn.esprit.autoloc.auditservice.entities.AuditLog;
import tn.esprit.autoloc.auditservice.entities.AuditSecurity;
import tn.esprit.autoloc.auditservice.repositories.AuditEventRepository;
import tn.esprit.autoloc.auditservice.repositories.AuditLogRepository;
import tn.esprit.autoloc.auditservice.repositories.AuditSecurityRepository;
import tn.esprit.autoloc.auditservice.util.JsonUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private static final String GENESIS = "GENESIS";

    private final AuditEventRepository eventRepository;
    private final AuditLogRepository logRepository;
    private final AuditSecurityRepository securityRepository;

    /**
     * Enregistre un événement : AuditEvent (brut) + AuditLog (chaîné) + AuditSecurity (si alerte).
     * synchronized : évite que deux logs prennent le même previousHash.
     * (pas de @Transactional ici : chaque save est validé immédiatement, dans le verrou)
     */
    public synchronized AuditLog record(AuditEventRequest req) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        AuditEvent event = eventRepository.save(AuditEvent.builder()
                .eventType(req.getEventType())
                .sourceService(req.getSourceService())
                .organizationId(req.getOrganizationId())
                .entityType(req.getEntityType())
                .entityId(req.getEntityId())
                .eventData(JsonUtil.toJson(req.getEventData()))
                .occurredAt(req.getOccurredAt() != null ? req.getOccurredAt() : now)
                .receivedAt(now)
                .build());

        Optional<AuditLog> last = logRepository.findFirstByOrderByCreatedAtDesc();
        String previousHash = last.map(AuditLog::getCurrentHash).orElse(GENESIS);

        // createdAt strictement croissant pour garder un ordre fiable de la chaîne
        Instant createdAt = now;
        if (last.isPresent() && !createdAt.isAfter(last.get().getCreatedAt())) {
            createdAt = last.get().getCreatedAt().plusMillis(1);
        }

        AuditLog log = AuditLog.builder()
                .eventId(event.getId())
                .userId(req.getUserId())
                .organizationId(req.getOrganizationId())
                .action(req.getAction())
                .entityType(req.getEntityType())
                .entityId(req.getEntityId())
                .description(req.getDescription())
                .previousHash(previousHash)
                .createdAt(createdAt)
                .build();
        log.setCurrentHash(computeHash(previousHash, log));
        // l'id est généré à l'insertion, donc on ne l'utilise pas dans le hash (on utilise eventId)
        log = logRepository.save(log);

        if (req.getSecurityType() != null && !req.getSecurityType().isBlank()) {
            securityRepository.save(AuditSecurity.builder()
                    .eventId(event.getId())
                    .userId(req.getUserId())
                    .organizationId(req.getOrganizationId())
                    .securityType(req.getSecurityType())
                    .severity(req.getSeverity() != null ? req.getSeverity() : "MEDIUM")
                    .ipAddress(req.getIpAddress())
                    .details(JsonUtil.toJson(req.getEventData()))
                    .detectedAt(now)
                    .build());
        }
        return log;
    }

    public List<AuditEvent> findAllEvents() {
        return eventRepository.findAll(Sort.by(Sort.Direction.DESC, "receivedAt"));
    }

    public List<AuditLog> searchLogs(String organizationId, String userId, String action,
                                     Instant from, Instant to) {
        return logRepository.search(blankToNull(organizationId), blankToNull(userId),
                blankToNull(action), from, to);
    }

    public AuditLog findLogById(String id) {
        return logRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "AuditLog introuvable : " + id));
    }

    public List<AuditSecurity> searchSecurity(String organizationId, String severity) {
        return securityRepository.search(blankToNull(organizationId), blankToNull(severity));
    }

    /** Parcourt toute la chaîne et recalcule chaque empreinte. */
    public IntegrityReport verifyIntegrity() {
        List<AuditLog> logs = logRepository.findAllByOrderByCreatedAtAsc();
        String expectedPrevious = GENESIS;
        long checked = 0;
        for (AuditLog log : logs) {
            checked++;
            boolean linkOk = expectedPrevious.equals(log.getPreviousHash());
            boolean hashOk = computeHash(log.getPreviousHash(), log).equals(log.getCurrentHash());
            if (!linkOk || !hashOk) {
                return new IntegrityReport(false, checked, log.getId(),
                        "Chaîne rompue : le log " + log.getId() + " a été modifié ou son chaînage est incohérent");
            }
            expectedPrevious = log.getCurrentHash();
        }
        return new IntegrityReport(true, checked, null, "Chaîne d'empreintes intacte");
    }

    private String computeHash(String previousHash, AuditLog l) {
        String data = String.join("|",
                nz(previousHash), nz(l.getEventId()), nz(l.getUserId()), nz(l.getOrganizationId()),
                nz(l.getAction()), nz(l.getEntityType()), nz(l.getEntityId()), nz(l.getDescription()),
                String.valueOf(l.getCreatedAt().toEpochMilli()));
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponible", e);
        }
    }

    private static String nz(String s) { return Objects.toString(s, ""); }

    private static String blankToNull(String s) { return (s == null || s.isBlank()) ? null : s; }
}