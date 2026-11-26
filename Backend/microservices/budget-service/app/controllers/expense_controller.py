
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.database.connection import get_db
from app.services.expense_service import ExpenseService

router = APIRouter(
    prefix="/expenses",
    tags=["Expenses"]
)


def get_expense_service(db: Session = Depends(get_db)):
    return ExpenseService(db)


@router.get("/")
def get_all_expenses(
        service: ExpenseService = Depends(get_expense_service)
):
    return service.get_all_expenses()


@router.get("/budget/{budget_id}")
def get_expenses_by_budget(
        budget_id: int,
        service: ExpenseService = Depends(get_expense_service)
):
    return service.get_expenses_by_budget(budget_id)


@router.get("/status/{status}")
def get_expenses_by_status(
        status: str,
        service: ExpenseService = Depends(get_expense_service)
):
    return service.get_expenses_by_status(status)


@router.get("/{expense_id}")
def get_expense_by_id(
        expense_id: int,
        service: ExpenseService = Depends(get_expense_service)
):
    expense = service.get_expense_by_id(expense_id)

    if expense is None:
        raise HTTPException(status_code=404, detail="Expense not found")

    return expense


@router.post("/", status_code=201)
def create_expense(
        expense_data: dict,
        service: ExpenseService = Depends(get_expense_service)
):
    expense = service.create_expense(expense_data)

    if expense is None:
        raise HTTPException(status_code=404, detail="Budget not found")

    return expense


@router.put("/{expense_id}")
def update_expense(
        expense_id: int,
        expense_data: dict,
        service: ExpenseService = Depends(get_expense_service)
):
    expense = service.update_expense(expense_id, expense_data)

    if expense is None:
        raise HTTPException(
            status_code=404,
            detail="Expense or associated budget not found"
        )

    return expense


@router.put("/{expense_id}/approve")
def approve_expense(
        expense_id: int,
        approver_id: int,
        service: ExpenseService = Depends(get_expense_service)
):
    expense = service.approve_expense(expense_id, approver_id)

    if expense is None:
        raise HTTPException(status_code=404, detail="Expense not found")

    return expense


@router.put("/{expense_id}/reject")
def reject_expense(
        expense_id: int,
        approver_id: int,
        comment: str,
        service: ExpenseService = Depends(get_expense_service)
):
    expense = service.reject_expense(
        expense_id,
        approver_id,
        comment
    )

    if expense is None:
        raise HTTPException(status_code=404, detail="Expense not found")

    return expense


@router.delete("/{expense_id}", status_code=204)
def delete_expense(
        expense_id: int,
        service: ExpenseService = Depends(get_expense_service)
):
    deleted = service.delete_expense(expense_id)

    if not deleted:
        raise HTTPException(status_code=404, detail="Expense not found")
