from app.models.budget import Budget
from app.models.expense import Expense
from app.models.budget_alert import BudgetAlert

print("Budget :", Budget.__tablename__)
print("Expense :", Expense.__tablename__)
print("BudgetAlert :", BudgetAlert.__tablename__)