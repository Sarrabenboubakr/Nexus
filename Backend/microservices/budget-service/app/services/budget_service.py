
from sqlalchemy.orm import Session
from sqlalchemy import func

from app.models.budget import Budget
from app.models.expense import Expense
from app.repositories.budget_repository import BudgetRepository


class BudgetService:

    def __init__(self, db: Session):
        self.db = db
        self.repository = BudgetRepository(db)

    def get_all_budgets(self):
        return self.repository.get_all()

    def get_budget_by_id(self, budget_id: int):
        return self.repository.get_by_id(budget_id)

    def get_budgets_by_project(self, project_id: int):
        return self.repository.get_by_project(project_id)

    def get_budgets_by_organization(self, organization_id: int):
        return self.repository.get_by_organization(organization_id)

    def create_budget(self, budget_data: dict):
        budget = Budget(**budget_data)
        return self.repository.create(budget)

    def update_budget(self, budget_id: int, budget_data: dict):
        budget = self.repository.get_by_id(budget_id)

        if budget is None:
            return None

        for key, value in budget_data.items():
            setattr(budget, key, value)

        return self.repository.update(budget)

    def delete_budget(self, budget_id: int):
        budget = self.repository.get_by_id(budget_id)

        if budget is None:
            return False

        self.repository.delete(budget)
        return True

    def get_budget_summary(self, budget_id: int):
        budget = self.repository.get_by_id(budget_id)

        if budget is None:
            return None

        consumed = self.db.query(
            func.coalesce(func.sum(Expense.amount), 0)
        ).filter(
            Expense.budget_id == budget_id,
            Expense.status == "APPROVED"
        ).scalar()

        remaining = budget.total_amount - consumed

        percentage = (
            float(consumed / budget.total_amount * 100)
            if budget.total_amount > 0 else 0
        )

        return {
            "budget_id": budget.id,
            "id": budget.id,
            "project_id": budget.project_id,
            "organization_id": budget.organization_id,
            "total_amount": budget.total_amount,
            "currency": budget.currency,
            "status": budget.status,
            "start_date": budget.start_date,
            "end_date": budget.end_date,
            "created_at": budget.created_at,
            "updated_at": budget.updated_at,
            "consumed_amount": consumed,
            "remaining_amount": remaining,
            "consumed_percentage": round(percentage, 2),
        }
