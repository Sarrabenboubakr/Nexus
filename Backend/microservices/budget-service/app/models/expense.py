"""Modèle SQLAlchemy de la table `expense` (copie exacte de ton schéma SQL)."""
from datetime import date, datetime
from decimal import Decimal
from enum import Enum
from typing import TYPE_CHECKING

from sqlalchemy import (
    TIMESTAMP,
    BigInteger,
    Date,
    FetchedValue,
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
    from app.models.budget_alert import BudgetAlert


class ExpenseStatus(str, Enum):
    """Valeurs autorisées pour expense.status (stockées en varchar)."""

    PENDING = "PENDING"      # dépense créée
    SUBMITTED = "SUBMITTED"  # envoyée pour approbation
    APPROVED = "APPROVED"    # approuvée
    REJECTED = "REJECTED"    # refusée


class Expense(Base):
    __tablename__ = "expense"
    __table_args__ = (
        Index("idx_expense_budget", "budget_id"),
        Index("idx_expense_status", "status"),
    )
    __mapper_args__ = {"eager_defaults": True}

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True, autoincrement=True)
    budget_id: Mapped[int] = mapped_column(
        BigInteger,
        ForeignKey("budget.id", ondelete="CASCADE", name="fk_expense_budget"),
        nullable=False,
    )
    category: Mapped[str] = mapped_column(String(100), nullable=False)
    amount: Mapped[Decimal] = mapped_column(Numeric(15, 2), nullable=False)
    description: Mapped[str | None] = mapped_column(Text)
    expense_date: Mapped[date] = mapped_column(Date, nullable=False)
    user_id: Mapped[int] = mapped_column(BigInteger, nullable=False)  # id externe
    status: Mapped[str | None] = mapped_column(
        String(20), server_default=text("'PENDING'")
    )
    approver_id: Mapped[int | None] = mapped_column(BigInteger)  # id externe
    comment: Mapped[str | None] = mapped_column(Text)
    created_at: Mapped[datetime] = mapped_column(
        TIMESTAMP, nullable=False, server_default=func.current_timestamp()
    )
    updated_at: Mapped[datetime] = mapped_column(
        TIMESTAMP,
        nullable=False,
        server_default=func.current_timestamp(),
        server_onupdate=FetchedValue(),
    )

    budget: Mapped["Budget"] = relationship(back_populates="expenses")
    # ON DELETE SET NULL : les alertes restent, expense_id devient NULL (géré par MySQL)
    alerts: Mapped[list["BudgetAlert"]] = relationship(
        back_populates="expense", passive_deletes=True
    )

    def __repr__(self) -> str:
        return f"<Expense id={self.id} budget_id={self.budget_id} amount={self.amount} status={self.status}>"