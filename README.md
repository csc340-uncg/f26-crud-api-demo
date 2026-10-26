# f26-crud-api-demo

# Plant Tracker API

A REST API for tracking houseplants, built with Spring Boot using a layered architecture. Data is stored in PostgreSQL (Neon).

This branch also includes a server-rendered MVC web interface for managing plants in a browser. The web interface and JSON API share the same service, repository, and database.

## Features

- Create, read, update, and delete plants
- Partial, case-insensitive search by name
- Filter by light requirements
- Browse, add, view, edit, and delete plants through a server-rendered web interface
- Upload plant pictures; plants without a picture use a placeholder
- Bean validation with clear HTTP status codes (400, 404)
- Configured entirely through environment variables (no secrets in the repo)

## Tech Stack

| Area      | Technology                              |
| --------- | --------------------------------------- |
| Language  | Java 25                                 |
| Framework | Spring Boot (Web, Data JPA, Validation) |
| Database  | PostgreSQL on [Neon](https://neon.com)  |
| Build     | Maven                                   |

Generate a starter project at [start.spring.io](https://start.spring.io) with: **Spring Web, Spring Data JPA, Validation, PostgreSQL Driver**.

## Architecture

The application has two controller layers for different clients. Both use the same service, repository, and entity layers.

```
Browser -- PlantUiController -- FreeMarker templates (HTML)
                  |
JSON client -- PlantController
                  |
             PlantService
                  |
          PlantRepository (JPA)
                  |
          Plant entity / PostgreSQL (Neon)
```

### Project Structure

```
src/main/java/com/csc340/plant_api/plants/
  PlantController.java
  PlantUiController.java
  PlantService.java
  PlantRepository.java
  Plant.java
  LightNeeds.java (enum)
  PlantNotFoundException.java
src/main/resources/
  application.properties
  templates/                 FreeMarker views for the MVC interface
  static/css/styles.css      Web interface styles
  static/thumbnails/         Uploaded pictures and placeholder image
```

## MVC Web Interface

Start the application and open `http://localhost:8080/` to use the browser interface.

| Method | Path                    | Description |
| ------ | ----------------------- | ----------- |
| GET    | `/` or `/plants`        | Plant gallery; optionally filter with `name` and `light` query parameters |
| GET    | `/plants/new`           | Show the add-plant form |
| POST   | `/plants`               | Create a plant and optionally upload its picture |
| GET    | `/plants/{id}`          | Show plant details |
| GET    | `/plants/{id}/edit`     | Show the edit form |
| POST   | `/plants/{id}`          | Update plant details and optionally replace its picture |
| POST   | `/plants/{id}/delete`   | Delete a plant |
| GET    | `/about`                | Explain the application and its request flow |

The gallery supports the same optional `name` and `light` filters as the JSON API. Plant pictures are served from `/thumbnails/`; when a plant has no picture, the interface displays `plant-placeholder.svg`.

## Data Model

**Plant** (table `plants`)

| Field                  | Type                            | Rules                   |
| ---------------------- | ------------------------------- | ----------------------- |
| `id`                   | Long                            | Generated automatically |
| `name`                 | String                          | Required, not blank     |
| `lightNeeds`           | Enum: `LOW`, `MEDIUM`, `BRIGHT` | Required                |
| `lastWatered`          | Date (`yyyy-MM-dd`)             | Required                |
| `wateringIntervalDays` | int                             | Must be positive        |
| `toxicToPets`          | boolean                         | Defaults to false       |
| `thumbnailUrl`         | String                          | Optional uploaded picture filename |

## API Reference

Base path: `/api/plants`

| Method | Path               | Description                              | Success |
| ------ | ------------------ | ---------------------------------------- | ------- |
| GET    | `/api/plants`      | List all plants (supports filters below) | 200     |
| GET    | `/api/plants/{id}` | Get one plant                            | 200     |
| POST   | `/api/plants`      | Create a plant                           | 201     |
| PUT    | `/api/plants/{id}` | Replace a plant's fields                 | 200     |
| DELETE | `/api/plants/{id}` | Delete a plant                           | 204     |

### Query parameters for `GET /api/plants`

| Parameter | Example                  | Behavior                                                 |
| --------- | ------------------------ | -------------------------------------------------------- |
| `name`    | `?name=mon`              | Partial, case-insensitive match ("mon" finds "Monstera") |
| `light`   | `?light=LOW`             | Exact match on `LOW`, `MEDIUM`, or `BRIGHT`              |
| both      | `?name=mon&light=MEDIUM` | Both filters must match                                  |

Blank or missing parameters are ignored.

### Example request

```http
POST /api/plants
Content-Type: application/json

{
  "name": "Monstera",
  "lightNeeds": "MEDIUM",
  "lastWatered": "2026-09-20",
  "wateringIntervalDays": 7,
  "toxicToPets": true
}
```

### Status codes

| Code | Meaning                                                                             |
| ---- | ----------------------------------------------------------------------------------- |
| 200  | OK                                                                                  |
| 201  | Created                                                                             |
| 204  | Deleted, no content                                                                 |
| 400  | Validation failed or malformed request (for example, an invalid `lightNeeds` value) |
| 404  | Plant with that id does not exist                                                   |

## Configuration

The app reads its database settings from environment variables. Spring Boot maps these names automatically, so nothing secret is stored in `application.properties`.

| Variable                | Example                                              |
| ----------------------- | ---------------------------------------------------- |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<neon-host>/<db>?sslmode=require` |
| `PORT`                  | Set by Render; defaults to `8080` locally            |

## Running Locally

1. Create a Neon project and gather your connection details.
2. Set the `SPRING_DATASOURCE_URL` environment variable.
   - Mac/Linux: `export SPRING_DATASOURCE_URL="jdbc:postgresql://<neon-host>/<db>?sslmode=require"`
   - Windows: `set SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<db>?sslmode=require`
   - PowerShell: `$env:SPRING_DATASOURCE_URL="jdbc:postgresql://<neon-host>/<db>?sslmode=require"`
3. Start the app:

```bash
./mvnw spring-boot:run        # Mac / Linux
.\mvnw.cmd spring-boot:run    # Windows
```

4. Visit `http://localhost:8080/` for the MVC web interface or `http://localhost:8080/api/plants` for the JSON API. A fresh database returns an empty plant list.

Hibernate creates the `plants` table automatically on first run (`spring.jpa.hibernate.ddl-auto=update`).

## Deployment (Render)

Render has no native Java runtime, so the app is deployed with Docker.

1. Add a `Dockerfile` at the repository root:

```dockerfile
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
```

2. Push the repository to GitHub.
3. In Render, create a **Web Service**, connect the repo, choose the **Docker** runtime and the **Free** instance type.
4. Add the `SPRING_DATASOURCE_URL` environment variable (value only, no quotes).
5. Deploy, then open `https://<your-service>.onrender.com/api/plants`.

### Free-tier notes

- Render free services sleep after roughly 15 minutes of inactivity, so the first request afterward can take up to a minute.
- Neon scales idle databases to zero, which adds a short delay on the first query.
- Render's file system is ephemeral. All persistent data lives in Neon.
