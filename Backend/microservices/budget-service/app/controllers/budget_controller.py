
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.database.connection import get_db
from app.services.budget_service import BudgetService

router = APIRouter(
    prefix="/budgets",
    tags=["Budgets"]
)


def get_budget_service(db: Session = Depends(get_db)):
    return BudgetService(db)


@router.get("/")
def get_all_budgets(service: BudgetService = Depends(get_budget_service)):
    return service.get_all_budgets()


@router.get("/{budget_id}")
def get_budget_by_id(
        budget_id: int,
        service: BudgetService = Depends(get_budget_service)
):
    budget = service.get_budget_by_id(budget_id)

    if budget is None:
        raise HTTPException(status_code=404, detail="Budget not found")

    return budget


@router.get("/{budget_id}/summary")
def get_budget_summary(
        budget_id: int,
        service: BudgetService = Depends(get_budget_service)
):
    summary = service.get_budget_summary(budget_id)

    if summary is None:
        raise HTTPException(status_code=404, detail="Budget not found")

    return summary


@router.get("/project/{project_id}")
def get_budgets_by_project(
        project_id: int,
        service: BudgetService = Depends(get_budget_service)
):
    return service.get_budgets_by_project(project_id)


@router.get("/organization/{organization_id}")
def get_budgets_by_organization(
        organization_id: int,
        service: BudgetService = Depends(get_budget_service)
):
    return service.get_budgets_by_organization(organization_id)


@router.post("/", status_code=201)
def create_budget(
        budget_data: dict,
        service: BudgetService = Depends(get_budget_service)
):
    return service.create_budget(budget_data)


@router.put("/{budget_id}")
def update_budget(
        budget_id: int,
        budget_data: dict,
        service: BudgetService = Depends(get_budget_service)
):
    budget = service.update_budget(budget_id, budget_data)

    if budget is None:
        raise HTTPException(status_code=404, detail="Budget not found")

    return budget


@router.delete("/{budget_id}", status_code=204)
def delete_budget(
        budget_id: int,
        service: BudgetService = Depends(get_budget_service)
):
    deleted = service.delete_budget(budget_id)

    if not deleted:
        raise HTTPException(status_code=404, detail="Budget not found")
