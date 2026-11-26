from sqlalchemy import text
from app.database.connection import engine

try:
    with engine.connect() as connection:
        result = connection.execute(text("SELECT DATABASE()"))
        print("Connexion réussie !")
        print("Base connectée :", result.scalar())

except Exception as e:
    print("Erreur de connexion :", e)