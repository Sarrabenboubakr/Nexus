
from sqlalchemy.orm import Session

from app.models.budget_alert import BudgetAlert
from app.repositories.alert_repository import AlertRepository
from app.repositories.budget_repository import BudgetRepository


class AlertService:

    def __init__(self, db: Session):
        self.db = db
        self.repository = AlertRepository(db)
        self.budget_repository = BudgetRepository(db)

    def get_all_alerts(self):
        return self.repository.get_all()

    def get_alert_by_id(self, alert_id: int):
        return self.repository.get_by_id(alert_id)

    def get_alerts_by_budget(self, budget_id: int):
        return self.repository.get_by_budget(budget_id)

    def get_unread_alerts(self):
        return self.repository.get_unread()

    def create_alert(self, alert_data: dict):
        budget = self.budget_repository.get_by_id(
            alert_data["budget_id"]
        )

        if budget is None:
            return None

        alert = BudgetAlert(**alert_data)
        return self.repository.create(alert)

    def mark_alert_as_read(self, alert_id: int):
        alert = self.repository.get_by_id(alert_id)

        if alert is None:
            return None

        return self.repository.mark_as_read(alert)

    def delete_alert(self, alert_id: int):
        alert = self.repository.get_by_id(alert_id)

        if alert is None:
            return False

        self.repository.delete(alert)
        return True
