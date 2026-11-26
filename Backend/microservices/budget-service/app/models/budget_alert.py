"""Modèle SQLAlchemy de la table `budget_alert` (copie exacte de ton schéma SQL)."""
from datetime import datetime
from decimal import Decimal
from enum import Enum
from typing import TYPE_CHECKING, Optional

from sqlalchemy import (
    TIMESTAMP,
    BigInteger,
    Boolean,
    ForeignKey,
    Index,
    Numeric,
    String,
    Text,
    func,
    text,
)
from sqlalchemy.orm import Mapped, mapped_column, relationship

from app.database.connection import Base

if TYPE_CHECKING:
    from app.models.budget import Budget
    from app.models.expense import Expense


class AlertType(str, Enum):
    """Valeurs autorisées pour budget_alert.type (varchar(30))."""

    THRESHOLD_80 = "THRESHOLD_80"
    OVERRUN = "OVERRUN"
    ANOMALY = "ANOMALY"
    FORECAST = "FORECAST"


class AlertLevel(str, Enum):
    """Valeurs autorisées pour budget_alert.level (varchar(20))."""

    INFO = "INFO"
    WARNING = "WARNING"
    CRITICAL = "CRITICAL"


class BudgetAlert(Base):
    __tablename__ = "budget_alert"
    __table_args__ = (
        Index("idx_alert_budget", "budget_id"),
        Index("idx_alert_expense", "expense_id"),
    )
    __mapper_args__ = {"eager_defaults": True}

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True, autoincrement=True)
    budget_id: Mapped[int] = mapped_column(
        BigInteger,
        ForeignKey("budget.id", ondelete="CASCADE", name="fk_alert_budget"),
        nullable=False,
    )
    expense_id: Mapped[int | None] = mapped_column(
        BigInteger,
        ForeignKey("expense.id", ondelete="SET NULL", name="fk_alert_expense"),
    )
    type: Mapped[str] = mapped_column(String(30), nullable=False)
    message: Mapped[str] = mapped_column(Text, nullable=False)
    level: Mapped[str | None] = mapped_column(
        String(20), server_default=text("'INFO'")
    )
    ai_score: Mapped[Decimal | None] = mapped_column(Numeric(5, 2))
    is_read: Mapped[bool | None] = mapped_column(
        Boolean, server_default=text("0")
    )
    created_at: Mapped[datetime] = mapped_column(
        TIMESTAMP, nullable=False, server_default=func.current_timestamp()
    )

    budget: Mapped["Budget"] = relationship(back_populates="alerts")
    expense: Mapped[Optional["Expense"]] = relationship(back_populates="alerts")

    def __repr__(self) -> str:
        return f"<BudgetAlert id={self.id} type={self.type} level={self.level}>"