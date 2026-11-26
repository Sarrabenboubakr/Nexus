
from sqlalchemy.orm import Session
from app.models.budget import Budget


class BudgetRepository:

    def __init__(self, db: Session):
        self.db = db

    def get_by_id(self, budget_id: int):
        return self.db.query(Budget).filter(
            Budget.id == budget_id
        ).first()

    def get_all(self):
        return self.db.query(Budget).all()

    def get_by_project(self, project_id: int):
        return self.db.query(Budget).filter(
            Budget.project_id == project_id
        ).all()

    def get_by_organization(self, organization_id: int):
        return self.db.query(Budget).filter(
            Budget.organization_id == organization_id
        ).all()

    def create(self, budget: Budget):
        self.db.add(budget)
        self.db.commit()
        self.db.refresh(budget)
        return budget

    def update(self, budget: Budget):
        self.db.commit()
        self.db.refresh(budget)
        return budget

    def delete(self, budget: Budget):
        self.db.delete(budget)
        self.db.commit()
