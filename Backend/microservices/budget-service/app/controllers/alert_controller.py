
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.database.connection import get_db
from app.services.alert_service import AlertService

router = APIRouter(
    prefix="/alerts",
    tags=["Alerts"]
)


def get_alert_service(db: Session = Depends(get_db)):
    return AlertService(db)


@router.get("/")
def get_all_alerts(
        service: AlertService = Depends(get_alert_service)
):
    return service.get_all_alerts()


@router.get("/unread")
def get_unread_alerts(
        service: AlertService = Depends(get_alert_service)
):
    return service.get_unread_alerts()


@router.get("/budget/{budget_id}")
def get_alerts_by_budget(
        budget_id: int,
        service: AlertService = Depends(get_alert_service)
):
    return service.get_alerts_by_budget(budget_id)


@router.get("/{alert_id}")
def get_alert_by_id(
        alert_id: int,
        service: AlertService = Depends(get_alert_service)
):
    alert = service.get_alert_by_id(alert_id)

    if alert is None:
        raise HTTPException(status_code=404, detail="Alert not found")

    return alert


@router.post("/", status_code=201)
def create_alert(
        alert_data: dict,
        service: AlertService = Depends(get_alert_service)
):
    alert = service.create_alert(alert_data)

    if alert is None:
        raise HTTPException(status_code=404, detail="Budget not found")

    return alert


@router.put("/{alert_id}/read")
def mark_alert_as_read(
        alert_id: int,
        service: AlertService = Depends(get_alert_service)
):
    alert = service.mark_alert_as_read(alert_id)

    if alert is None:
        raise HTTPException(status_code=404, detail="Alert not found")

    return alert


@router.delete("/{alert_id}", status_code=204)
def delete_alert(
        alert_id: int,
        service: AlertService = Depends(get_alert_service)
):
    deleted = service.delete_alert(alert_id)

    if not deleted:
        raise HTTPException(status_code=404, detail="Alert not found")
