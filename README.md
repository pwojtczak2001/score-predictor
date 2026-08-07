# ⚽ Score Predictor

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-success)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-brightgreen)
![Database](https://img.shields.io/badge/Database-Microsoft_SQL_Server-red)
![Build](https://img.shields.io/badge/Build-Maven-blue)

A modern football prediction platform built with **Java** and **Spring Boot**.

The application allows users to create private leagues, predict football match results, compete with friends, and automatically calculate rankings based on prediction accuracy.

The backend follows a layered architecture and exposes a REST API secured with **Spring Security** and **JWT authentication**.

---

# 📖 Overview

Score Predictor is designed to automate the entire football prediction workflow.

The application:

- imports football matches from external JSON data,
- synchronizes match information,
- allows users to register and authenticate,
- enables users to create or join private leagues,
- allows predicting match scores,
- automatically locks predictions after kick-off,
- awards points when matches finish,
- updates league rankings automatically.

The project focuses on clean architecture, separation of concerns and business logic rather than simple CRUD operations.

---

# 🎮 How It Works

The application follows the workflow below:

1. Teams are imported from external JSON files.
2. Football matches are imported into the database.
3. Existing matches are synchronized automatically.
4. Users create an account.
5. Users authenticate using JWT.
6. Users create or join private leagues.
7. Users predict upcoming football matches.
8. Predictions become unavailable once a match starts.
9. Finished matches are synchronized.
10. Prediction points are calculated automatically.
11. League rankings are updated automatically.

---

# 🏆 Scoring System

Prediction points are awarded automatically after every finished match.

| Prediction                                | Points |
|-------------------------------------------|-------:|
| Exact score                               |  **3** |
| Correct match outcome (win / draw / loss) |  **1** |
| Incorrect prediction                      |  **0** |

### Examples

| Actual Result | Prediction | Awarded Points |
|---------------|------------|---------------:|
| 2–1           | 2–1        |          **3** |
| 2–1           | 4–3        |          **1** |
| 2–1           | 1–1        |          **0** |

When a match changes its status to **FINISHED**, the application automatically:

- evaluates every prediction,
- calculates awarded points,
- stores awarded points,
- updates every league member's total score,
- refreshes league rankings.

---

# ✨ Current Features

## User Management

- ✅ User registration
- ✅ User authentication
- ✅ JWT authentication
- ✅ BCrypt password hashing
- ✅ Stateless authentication
- ✅ Get authenticated user profile

---

## Football Data

- ✅ Import teams from JSON
- ✅ Import matches from JSON
- ✅ Prevent duplicate teams
- ✅ Prevent duplicate matches
- ✅ Synchronize existing matches
- ✅ Synchronize match dates
- ✅ Synchronize match statuses
- ✅ Synchronize match results

---

## Private Leagues

- ✅ League domain model
- ✅ Unique invitation code generation
- ✅ League membership management
- ✅ League ranking calculation
- 🚧 League creation endpoint
- 🚧 League join endpoint

---

## Match Predictions

- ✅ Create predictions
- ✅ Update predictions
- ✅ One prediction per user per match
- ✅ Automatic prediction locking after kick-off
- ✅ Prediction validation

---

## Automatic Scoring

- ✅ Automatic point calculation
- ✅ Exact score evaluation
- ✅ Correct winner/draw evaluation
- ✅ Automatic league score updates
- ✅ Automatic ranking generation

---

## Security

- ✅ Spring Security
- ✅ JWT Authentication
- ✅ Custom JWT Authentication Filter
- ✅ AuthenticationManager
- ✅ SecurityContextHolder
- ✅ Stateless sessions
- ✅ Custom UserDetailsService

---

# 📥 Match Import & Synchronization

Football data is imported from external JSON files generated from Flashscore.

The import process automatically:

- imports new teams,
- imports new matches,
- prevents duplicate teams,
- prevents duplicate matches,
- synchronizes match dates,
- synchronizes match statuses,
- synchronizes final scores.

Whenever a match changes its status to **FINISHED**, the application automatically starts the scoring process and updates league rankings.

---

# 🛠 Tech Stack

## Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT (JJWT)
- Jackson

## Database

- Microsoft SQL Server

## Build Tool

- Maven

---

# 🏛 Architecture

The application follows a layered architecture.

```
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Database
```

Business logic is isolated inside services while controllers expose REST endpoints only.

This separation makes the application easier to maintain, extend and test.

---

# 🗄 Domain Model

The application is built around the following entities.

```
User
 │
 ├──────────────┐
 │              │
 ▼              ▼
Prediction   LeagueMember
 │              │
 ▼              ▼
Match       League
 │
 ├──────────────┐
 ▼              ▼
Home Team   Away Team
```

Main entities:

- User
- Team
- Match
- Prediction
- League
- LeagueMember

---

# 🔐 Authentication

Authentication is based on JSON Web Tokens (JWT).

Authentication flow:

1. User sends credentials.

```
POST /auth/login
```

1. Spring Security authenticates the user.

2. AuthenticationManager verifies credentials.

3. A JWT token is generated.

4. The frontend stores the token.

5. Every protected request includes:

```
Authorization: Bearer <JWT_TOKEN>
```

6. JwtAuthenticationFilter validates the token.

7. The authenticated user is stored inside SecurityContextHolder.

8. Spring Security grants access to protected resources.

---

# 📊 UML Diagrams

The following UML diagrams document the architecture and authentication flow of the application.

## Class Diagram

![Class Diagram](images/Class_Diagram.png)

---

## Login Sequence Diagram

![Login Sequence](images/Sequence_Diagram_Login.png)

---

## Registration Sequence Diagram

![Registration Sequence](images/Sequence_Diagram_Register.png)

---

## JWT Authentication Sequence Diagram

![JWT Authentication](images/Authentication_Sequence_Diagram.png)

---

## Authentication Activity Diagram

Shows all possible authentication paths:

- successful login
- invalid password
- missing token
- invalid token
- valid token

![Authentication Activity](images/Activity_Diagram.png)

---

## Entity Relationship Diagram

![ERD](images/ERD_Diagram.png)

---

# ⚙ REST API

## Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user and return a JWT token |
| POST | `/auth/login` | Authenticate a user and return a JWT token |


## Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users/me` | Get the authenticated user's profile |


## Matches

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/matches/upcoming` | Get upcoming matches together with the authenticated user's predictions |


## Predictions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/predictions` | Create a new prediction for a match |
| PUT | `/predictions` | Update an existing prediction before kick-off |


## Leagues

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/leagues/{leagueId}/ranking` | Get the ranking of a private league |

---

# 📁 Project Structure

```
score-predictor
│
├── images/
│   ├── Activity_Diagram.png
│   ├── Authentication_Sequence_Diagram.png
│   ├── Class_Diagram.png
│   ├── ERD_Diagram.png
│   ├── Sequence_Diagram_Login.png
│   └── Sequence_Diagram_Register.png
│
├── src
│   ├── config
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   ├── runner
│   ├── security
│   └── service
│
├── pom.xml
│
└── README.md
```

---

# 🚀 Planned Features

- React frontend
- Responsive UI
- Role-based authorization (USER / ADMIN)

---

# ▶ Getting Started

## Clone the repository

```bash
git clone https://github.com/your-account/score-predictor.git
```

## Build the project

```bash
mvn clean install
```

## Run the application

```bash
mvn spring-boot:run
```

---

# 👨‍💻 Author

**Piotr Wojtczak**

Java Backend Developer

Portfolio project built to demonstrate backend development skills using Java, Spring Boot, Spring Security, REST APIs and Microsoft SQL Server.