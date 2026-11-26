
from sqlalchemy.orm import Session
from app.models.expense import Expense


class ExpenseRepository:

    def __init__(self, db: Session):
        self.db = db

    def get_by_id(self, expense_id: int):
        return self.db.query(Expense).filter(
            Expense.id == expense_id
        ).first()

    def get_all(self):
        return self.db.query(Expense).all()

    def get_by_budget(self, budget_id: int):
        return self.db.query(Expense).filter(
            Expense.budget_id == budget_id
        ).all()

    def get_by_status(self, status: str):
        return self.db.query(Expense).filter(
            Expense.status == status
        ).all()

    def create(self, expense: Expense):
        self.db.add(expense)
        self.db.commit()
        self.db.refresh(expense)
        return expense

    def update(self, expense: Expense):
        self.db.commit()
        self.db.refresh(expense)
        return expense

    def delete(self, expense: Expense):
        self.db.delete(expense)
        self.db.commit()
