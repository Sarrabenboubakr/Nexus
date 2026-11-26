"""Schemas Pydantic pour les budgets : validation des entrées et forme des sorties."""
from datetime import date, datetime
from decimal import Decimal

from pydantic import BaseModel, ConfigDict, Field, field_validator, model_validator

from app.models.budget import BudgetStatus


class _BudgetBase(BaseModel):
    # use_enum_values   : stocke "ACTIVE" (texte) et non BudgetStatus.ACTIVE, pour la base
    # validate_default  : applique aussi cette conversion aux valeurs par défaut
    # str_strip_whitespace : enlève les espaces au début et à la fin des textes
    model_config = ConfigDict(
        use_enum_values=True,
        validate_default=True,
        str_strip_whitespace=True,
    )


def _check_dates(start: date | None, end: date | None) -> None:
    if start is not None and end is not None and end < start:
        raise ValueError("end_date doit être postérieure ou égale à start_date")


class BudgetCreate(_BudgetBase):
    """Données pour créer un budget."""

    project_id: int = Field(gt=0)          # id externe (autre microservice)
    organization_id: int = Field(gt=0)     # id externe (autre microservice)
    total_amount: Decimal = Field(gt=0, max_digits=15, decimal_places=2)
    currency: str = Field(default="TND", min_length=1, max_length=10)
    status: BudgetStatus = BudgetStatus.ACTIVE
    start_date: date | None = None
    end_date: date | None = None

    @field_validator("currency")
    @classmethod
    def currency_uppercase(cls, value: str) -> str:
        return value.upper()

    @model_validator(mode="after")
    def dates_are_coherent(self) -> "BudgetCreate":
        _check_dates(self.start_date, self.end_date)
        return self


class BudgetUpdate(_BudgetBase):
    """Données pour modifier un budget. Tous les champs sont optionnels :
    seuls ceux envoyés sont modifiés. project_id et organization_id ne sont
    volontairement pas modifiables."""

    total_amount: Decimal | None = Field(
        default=None, gt=0, max_digits=15, decimal_places=2
    )
    currency: str | None = Field(default=None, min_length=1, max_length=10)
    status: BudgetStatus | None = None
    start_date: date | None = None
    end_date: date | None = None

    @field_validator("currency")
    @classmethod
    def currency_uppercase(cls, value: str | None) -> str | None:
        return value.upper() if value is not None else None

    @model_validator(mode="after")
    def check_update(self) -> "BudgetUpdate":
        if not self.model_fields_set:
            raise ValueError("Aucun champ à modifier")
        _check_dates(self.start_date, self.end_date)
        return self


class BudgetResponse(BaseModel):
    """Budget renvoyé au client. from_attributes=True permet de le construire
    directement depuis un objet SQLAlchemy Budget."""

    model_config = ConfigDict(from_attributes=True)

    id: int
    project_id: int
    organization_id: int
    total_amount: Decimal
    currency: str | None
    status: str | None  # str (pas l'enum) : on affiche ce qui est en base sans planter
    start_date: date | None
    end_date: date | None
    created_at: datetime
    updated_at: datetime


class BudgetSummaryResponse(BudgetResponse):
    """Budget + chiffres calculés par le service (jamais stockés en base)."""

    consumed_amount: Decimal       # somme des dépenses APPROVED
    remaining_amount: Decimal      # total_amount - consumed_amount (peut être négatif)
    consumption_percent: Decimal   # consumed_amount / total_amount * 100