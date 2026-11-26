
from fastapi import FastAPI

from app.controllers.budget_controller import router as budget_router
from app.controllers.expense_controller import router as expense_router
from app.controllers.alert_controller import router as alert_router

app = FastAPI(
    title="Budget Microservice",
    description="API for managing budgets, expenses and alerts",
    version="1.0.0"
)

app.include_router(budget_router)
app.include_router(expense_router)
app.include_router(alert_router)


@app.get("/")
def home():
    return {
        "message": "Budget Microservice is running"
    }
