from datetime import datetime
from decimal import Decimal

from pydantic import BaseModel, ConfigDict, Field

from app.models.budget_alert import AlertLevel, AlertType


class AlertCreate(BaseModel):
    model_config = ConfigDict(extra="forbid", use_enum_values=True, validate_default=True)

    budget_id: int = Field(gt=0)
    type: AlertType
    message: str = Field(min_length=1)
    expense_id: int | None = Field(default=None, gt=0)
    level: AlertLevel = AlertLevel.INFO
    ai_score: Decimal | None = Field(default=None, max_digits=5, decimal_places=2)
    is_read: bool = False


class AlertResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    budget_id: int
    expense_id: int | None
    type: str
    message: str
    level: str | None
    ai_score: Decimal | None
    is_read: bool | None
    created_at: datetime
