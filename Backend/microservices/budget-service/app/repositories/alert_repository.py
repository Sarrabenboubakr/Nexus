
from sqlalchemy.orm import Session
from app.models.budget_alert import BudgetAlert


class AlertRepository:

    def __init__(self, db: Session):
        self.db = db

    def get_by_id(self, alert_id: int):
        return self.db.query(BudgetAlert).filter(
            BudgetAlert.id == alert_id
        ).first()

    def get_all(self):
        return self.db.query(BudgetAlert).all()

    def get_by_budget(self, budget_id: int):
        return self.db.query(BudgetAlert).filter(
            BudgetAlert.budget_id == budget_id
        ).all()

    def get_unread(self):
        return self.db.query(BudgetAlert).filter(
            BudgetAlert.is_read == False
        ).all()

    def create(self, alert: BudgetAlert):
        self.db.add(alert)
        self.db.commit()
        self.db.refresh(alert)
        return alert

    def mark_as_read(self, alert: BudgetAlert):
        alert.is_read = True
        self.db.commit()
        self.db.refresh(alert)
        return alert

    def delete(self, alert: BudgetAlert):
        self.db.delete(alert)
        self.db.commit()
