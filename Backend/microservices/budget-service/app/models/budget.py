"""Modèle SQLAlchemy de la table `budget` (copie exacte de ton schéma SQL)."""
from datetime import date, datetime
from decimal import Decimal
from enum import Enum
from typing import TYPE_CHECKING

from sqlalchemy import (
    TIMESTAMP,
    BigInteger,
    Date,
    FetchedValue,
    Numeric,
    String,
    func,
    text,
)
from sqlalchemy.orm import Mapped, mapped_column, relationship

from app.database.connection import Base

if TYPE_CHECKING:
    from app.models.budget_alert import BudgetAlert
    from app.models.expense import Expense


class BudgetStatus(str, Enum):
    """Valeurs autorisées pour budget.status (stockées en varchar)."""

    ACTIVE = "ACTIVE"
    CLOSED = "CLOSED"
    EXCEEDED = "EXCEEDED"


class Budget(Base):
    __tablename__ = "budget"
    # Après un INSERT/UPDATE, recharge les valeurs générées par MySQL (id, dates...)
    __mapper_args__ = {"eager_defaults": True}

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True, autoincrement=True)
    project_id: Mapped[int] = mapped_column(BigInteger, nullable=False)  # id externe
    organization_id: Mapped[int] = mapped_column(BigInteger, nullable=False)  # id externe
    total_amount: Mapped[Decimal] = mapped_column(Numeric(15, 2), nullable=False)
    currency: Mapped[str | None] = mapped_column(
        String(10), server_default=text("'TND'")
    )
    status: Mapped[str | None] = mapped_column(
        String(20), server_default=text("'ACTIVE'")
    )
    start_date: Mapped[date | None] = mapped_column(Date)
    end_date: Mapped[date | None] = mapped_column(Date)
    created_at: Mapped[datetime] = mapped_column(
        TIMESTAMP, nullable=False, server_default=func.current_timestamp()
    )
    updated_at: Mapped[datetime] = mapped_column(
        TIMESTAMP,
        nullable=False,
        server_default=func.current_timestamp(),
        server_onupdate=FetchedValue(),  # MySQL gère ON UPDATE current_timestamp()
    )

    # ON DELETE CASCADE est fait par MySQL : passive_deletes=True évite que
    # SQLAlchemy recharge et supprime les enfants un par un.
    expenses: Mapped[list["Expense"]] = relationship(
        back_populates="budget", cascade="all, delete-orphan", passive_deletes=True
    )
    alerts: Mapped[list["BudgetAlert"]] = relationship(
        back_populates="budget", cascade="all, delete-orphan", passive_deletes=True
    )

    def __repr__(self) -> str:
        return f"<Budget id={self.id} project_id={self.project_id} total={self.total_amount}>"