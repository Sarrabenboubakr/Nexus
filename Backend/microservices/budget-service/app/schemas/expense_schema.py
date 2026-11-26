from datetime import date, datetime
from decimal import Decimal

from pydantic import BaseModel, ConfigDict, Field, model_validator

from app.models.expense import ExpenseStatus


class _ExpenseBase(BaseModel):
    model_config = ConfigDict(
        extra="forbid",
        use_enum_values=True,
        validate_default=True,
        str_strip_whitespace=True,
    )


class ExpenseCreate(_ExpenseBase):
    budget_id: int = Field(gt=0)
    category: str = Field(min_length=1, max_length=100)
    amount: Decimal = Field(gt=0, max_digits=15, decimal_places=2)
    expense_date: date
    user_id: int = Field(gt=0)
    description: str | None = None
    status: ExpenseStatus = ExpenseStatus.PENDING
    approver_id: int | None = Field(default=None, gt=0)
    comment: str | None = None


class ExpenseUpdate(_ExpenseBase):
    budget_id: int | None = Field(default=None, gt=0)
    category: str | None = Field(default=None, min_length=1, max_length=100)
    amount: Decimal | None = Field(default=None, gt=0, max_digits=15, decimal_places=2)
    expense_date: date | None = None
    user_id: int | None = Field(default=None, gt=0)
    description: str | None = None
    status: ExpenseStatus | None = None
    approver_id: int | None = Field(default=None, gt=0)
    comment: str | None = None

    @model_validator(mode="after")
    def at_least_one_field(self) -> "ExpenseUpdate":
        if not self.model_fields_set:
            raise ValueError("Au moins un champ doit être fourni")
        return self


class ExpenseResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    budget_id: int
    category: str
    amount: Decimal
    description: str | None
    expense_date: date
    user_id: int
    status: str | None
    approver_id: int | None
    comment: str | None
    created_at: datetime
    updated_at: datetime
