# nexus_4DAS16-g4
laborateur du dépôt distant une fois le dépôt publié.
# NEXUS – Plateforme de gestion collaborative

## 1. Présentation du projet

**NEXUS** est une plateforme de gestion collaborative développée selon une architecture microservices. Elle a pour objectif de faciliter la gestion des organisations, des projets, des tâches, des documents, des collaborations, des budgets et des utilisateurs.

La plateforme repose sur des microservices indépendants, chacun responsable d'un domaine fonctionnel précis. Cette architecture permet une meilleure modularité, une séparation des responsabilités et une évolution indépendante des différents services.

## 2. Membres de l'équipe et répartition du travail

Chaque membre de l'équipe est responsable du développement d'un microservice.

| Membre | Microservice |
|---|---|
| Arij Latrech | Organization Service |
| Aziz Dawahi | Project Service |
| Chaima Mazitti | Task Service |
| Yosr Beh Hamouda | Audit Service |
| Makrem Mihoubi | Documentation Service |
| Sarra Ben Boubaker | Collaboration Service |

### Microservices communs

Les microservices suivants sont développés et partagés par l'ensemble de l'équipe :

- **Budget Service**
- **Notification Service**
- **User Service**

## 3. Architecture du projet

Le projet adopte une architecture distribuée basée sur plusieurs microservices indépendants.

Les principaux composants sont :

- **Eureka Server :** assure l'enregistrement et la découverte dynamique des microservices.
- **Spring Boot :** framework utilisé pour le développement des microservices Java.
- **Python :** utilisé pour le développement du microservice Budget.
- **Spring Actuator :** permet la surveillance et l'observabilité des applications.
- **MySQL :** système de gestion des bases de données.
- **Maven :** outil de gestion des dépendances et de construction des projets.

## 4. Microservices

### 4.1 Microservices individuels

| Microservice | Responsable | Fonction principale |
|---|---|---|
| Organization Service | Arij Latrech | Gestion des organisations |
| Project Service | Aziz Dawahi | Gestion des projets |
| Task Service | Chaima Mazitti | Gestion des tâches |
| Audit Service | Yosr Beh Hamouda | Traçabilité des opérations |
| Documentation Service | Makrem Mihoubi | Gestion des documents |
| Collaboration Service | Sarra Ben Boubaker | Gestion de la collaboration |

### 4.2 Microservices communs

| Microservice | Fonction principale |
|---|---|
| Budget Service | Gestion des budgets et des dépenses |
| Notification Service | Gestion et envoi des notifications |
| User Service | Gestion des utilisateurs |

Chaque microservice dispose de ses propres entités, associations et opérations CRUD.

## 5. Eureka Server – Service Discovery

Le projet utilise **Netflix Eureka** pour assurer l'enregistrement et la découverte dynamique des microservices.

Tous les microservices sont configurés comme clients Eureka afin de pouvoir s'enregistrer auprès des serveurs.

### Configuration des instances Eureka

Deux instances Eureka sont mises en place sur des ports différents :

| Instance | Port |
|---|---|
| Eureka Server 1 | 8761 |
| Eureka Server 2 | 8762 |

Dashboards :

- Eureka 1 : http://localhost:8761
- Eureka 2 : http://localhost:8762

Les microservices sont enregistrés auprès des serveurs Eureka et peuvent être découverts dynamiquement.

## 6. Déploiement multi-instances

Afin de démontrer la capacité de l'architecture à gérer plusieurs instances d'un même service, deux microservices sont exécutés avec au moins deux instances chacun, sur des ports différents.

Les différentes instances sont enregistrées auprès des serveurs Eureka, ce qui permet leur découverte et leur suivi depuis les dashboards.

## 7. Observabilité avec Spring Boot Actuator

Spring Boot Actuator est intégré aux microservices Spring Boot afin de surveiller leur état de fonctionnement et d'obtenir des informations sur leurs performances.

### Endpoints utilisés

| Endpoint | Description |
|---|---|
| `/actuator/health` | Vérification de l'état de santé du service |
| `/actuator/info` | Informations sur l'application |
| `/actuator/metrics` | Consultation des métriques |
| `/actuator/beans` | Liste des beans Spring |
| `/actuator/mappings` | Liste des mappings HTTP |

Ces endpoints permettent de suivre l'état des applications et de faciliter leur supervision.

## 8. Fonctionnalités CRUD

Chaque microservice dispose d'opérations CRUD permettant de :

- Créer des enregistrements.
- Consulter les données.
- Modifier les enregistrements.
- Supprimer les enregistrements.

Les opérations sont exposées à travers des API REST.

## 9. Installation et exécution du projet

### Prérequis

- Java JDK
- Python
- Maven
- MySQL
- Git

### Étapes de lancement

1. Cloner le dépôt GitHub.
2. Configurer les bases de données nécessaires.
3. Démarrer les deux instances Eureka.
4. Démarrer les microservices.
5. Démarrer les instances supplémentaires des deux microservices choisis.
6. Vérifier l'enregistrement des services dans les dashboards Eureka.
7. Tester les opérations CRUD.
8. Vérifier les endpoints Actuator.

## 10. Technologies utilisées

| Technologie | Utilisation |
|---|---|
| Java / Spring Boot | Développement des microservices |
| Spring Cloud Netflix Eureka | Service Discovery |
| Spring Boot Actuator | Observabilité |
| Python | Microservice Budget |
| MySQL | Bases de données |
| Maven | Gestion des dépendances |
| Git / GitHub | Gestion du code source |

## 11. capture dashbird eureka 





## 12. Dépôt GitHub

Lien du projet :

https://github.com/Sarrabenboubakr/Nexus

Le dépôt contient le code source, les configurations et les différents microservices développés par l'équipe.

---

**Projet réalisé dans le cadre du module Architecture des Applications Distribuées – ESPRIT.**
