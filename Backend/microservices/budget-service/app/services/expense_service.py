
from sqlalchemy.orm import Session

from app.models.expense import Expense
from app.repositories.expense_repository import ExpenseRepository
from app.repositories.budget_repository import BudgetRepository


class ExpenseService:

    def __init__(self, db: Session):
        self.db = db
        self.repository = ExpenseRepository(db)
        self.budget_repository = BudgetRepository(db)

    def get_all_expenses(self):
        return self.repository.get_all()

    def get_expense_by_id(self, expense_id: int):
        return self.repository.get_by_id(expense_id)

    def get_expenses_by_budget(self, budget_id: int):
        return self.repository.get_by_budget(budget_id)

    def get_expenses_by_status(self, status: str):
        return self.repository.get_by_status(status)

    def create_expense(self, expense_data: dict):
        budget = self.budget_repository.get_by_id(
            expense_data["budget_id"]
        )

        if budget is None:
            return None

        expense = Expense(**expense_data)
        return self.repository.create(expense)

    def update_expense(self, expense_id: int, expense_data: dict):
        expense = self.repository.get_by_id(expense_id)

        if expense is None:
            return None

        if "budget_id" in expense_data:
            budget = self.budget_repository.get_by_id(
                expense_data["budget_id"]
            )
            if budget is None:
                return None

        for key, value in expense_data.items():
            setattr(expense, key, value)

        return self.repository.update(expense)

    def delete_expense(self, expense_id: int):
        expense = self.repository.get_by_id(expense_id)

        if expense is None:
            return False

        self.repository.delete(expense)
        return True

    def approve_expense(self, expense_id: int, approver_id: int):
        expense = self.repository.get_by_id(expense_id)

        if expense is None:
            return None

        expense.status = "APPROVED"
        expense.approver_id = approver_id

        return self.repository.update(expense)

    def reject_expense(
            self,
            expense_id: int,
            approver_id: int,
            comment: str
    ):
        expense = self.repository.get_by_id(expense_id)

        if expense is None:
            return None

        expense.status = "REJECTED"
        expense.approver_id = approver_id
        expense.comment = comment

        return self.repository.update(expense)
