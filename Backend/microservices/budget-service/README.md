# Microservice Budget Python

L’API FastAPI utilise directement la base MariaDB/MySQL `budget_db` et ses tables `budget`, `expense` et `budget_alert`.

## 1. Importer la base

Le fichier SQL fourni crée les tables mais suppose que la base `budget_db` existe déjà.

Dans phpMyAdmin :

1. Crée la base `budget_db` si elle n’existe pas.
2. Sélectionne cette base, ouvre **Importer**, choisis `budget_db (2).sql` et lance l’import.

Vérifie que les trois tables `budget`, `expense` et `budget_alert` apparaissent. MySQL/MariaDB doit écouter sur le port `3306`.

## 2. Installer et démarrer l’API (PowerShell)

Depuis la racine du dépôt :

```powershell
cd Backend/microservices/budget-service
py -3.12 -m venv .venv
.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
python -m uvicorn app.main:app --reload
```

La configuration par défaut est `root` sans mot de passe, sur `localhost:3306`. Si ton MySQL utilise d’autres identifiants, configure la connexion avant le démarrage :

```powershell
$env:DATABASE_URL = "mysql+pymysql://root:TON_MOT_DE_PASSE@localhost:3306/budget_db?charset=utf8mb4"
```

Garde la fenêtre PowerShell ouverte pendant les essais.

## 3. Tester avec Swagger

Ouvre <http://127.0.0.1:8000/docs>. La route `/` doit répondre que le microservice est démarré.

Dans Swagger, ouvre **POST `/budgets/` → Try it out** et essaie :

```json
{
  "project_id": 1,
  "organization_id": 1,
  "total_amount": 1000,
  "currency": "TND"
}
```

Garde l’`id` retourné pour tester **GET `/budgets/{budget_id}`**, **PUT `/budgets/{budget_id}`**, **GET `/budgets/{budget_id}/summary`**, puis **DELETE `/budgets/{budget_id}`**.

Pour créer une dépense, utilise **POST `/expenses/`** avec `budget_id` égal à l’id du budget créé, ainsi qu’un `category`, `amount`, `expense_date` (format `YYYY-MM-DD`) et `user_id`. Pour créer une alerte, utilise **POST `/alerts/`** avec `budget_id`, `type` (par exemple `THRESHOLD_80`) et `message`.
