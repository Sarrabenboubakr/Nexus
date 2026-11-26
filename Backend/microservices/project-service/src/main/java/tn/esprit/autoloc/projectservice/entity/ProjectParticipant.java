package tn.esprit.autoloc.projectservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_participant")
@Getter
@Setter
@NoArgsConstructor
public class ProjectParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    private Integer organizationId;
    private Integer partnershipId;

    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime invitedAt;

    private LocalDateTime joinedAt; // null tant que l'invitation n'est pas acceptée
}