from enum import Enum


class ExpenseStatus(str, Enum):
    EN_ATTENTE = "EN_ATTENTE"
    VALIDEE = "VALIDEE"
    REFUSEE = "REFUSEE"


class BudgetStatus(str, Enum):
    ACTIF = "ACTIF"
    CLOTURE = "CLOTURE"


class AlertType(str, Enum):
    SEUIL_80 = "SEUIL_80"
    DEPASSEMENT = "DEPASSEMENT"
    ANOMALIE_IA = "ANOMALIE_IA"
    PREVISION_IA = "PREVISION_IA"