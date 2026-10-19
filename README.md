# Mapersive — Map Layer API refactoring exercise

**Time: 30 minutes.** This is a deliberately small Spring Boot backend, with no frontend or AWS setup.

## Application description

Mapersive users organize map layers into projects. A layer has a generated ID, a project ID, a display name, and a type. This API creates a layer and lists the layers belonging to one project. Data is stored in an in-memory H2 database and disappears when the application stops. No project table exists: assume any positive project ID is valid. Duplicate layer names are allowed.

## Setup (before the timer starts)

Use JDK 17 or 21 and Maven 3.6.3 or newer. Open this directory as a Maven project in your IDE, or run:

```bash
mvn test
mvn spring-boot:run
```

The API starts at `http://localhost:8080`. The interviewer should resolve dependency downloads and setup issues before starting the timer. Maven is also available through many Java IDEs. The dependencies are pinned for this exercise; no framework upgrade is requested.

## Expected API contract

### Create a layer

`POST /api/layers`, with `Content-Type: application/json`:

```json
{"projectId":1000,"name":"Roads","type":"VECTOR"}
```

- `projectId` is a required positive integer representable by Java `Long`.
- `name` is required; trim leading/trailing whitespace, then require 1–100 characters.
- `type` is required and must be exactly `VECTOR` or `RASTER` (case-sensitive).
- Success: **201 Created**, JSON containing `id`, `projectId`, normalized `name`, and `type`.
- Invalid input: **400 Bad Request**, a JSON object with a readable `error` message. Wording is not prescribed; do not expose exception internals.
- Unexpected server errors must not be reported as successful requests.

### List a project's layers

`GET /api/projects/{projectId}/layers`

- `projectId` must be a positive Java `Long`.
- Success: **200 OK**, a JSON array containing only that project's layers, each with the same four fields as the create response.
- No layers: **200 OK** with `[]`. Result order is unspecified.
- Invalid project ID: **400 Bad Request**, JSON with an `error` message.

These are the intended requirements. The current implementation does not satisfy all of them. Correcting those discrepancies is explicitly part of this exercise; preserve endpoint paths and valid response field names.

## Your task

1. Inspect the code and briefly describe the highest-priority problems.
2. Refactor to make the request handling, application logic, and database access easier to maintain and test.
3. Improve correctness, input validation, and HTTP error handling against the contract above.
4. Add focused tests for the changes you prioritize. Existing tests are smoke tests, not proof of full correctness.
5. Explain what you finished and what you would do next. You are not expected to address every possible improvement in 30 minutes.

Start with `LayerController.java`, `LayerRepository.java`, and `MapLayer.java`. Prefer small, justified changes. You may create classes and modify tests. No authentication, pagination, deployment, database migration, frontend, or framework upgrade is required.

## Manual requests

```bash
curl -i -X POST http://localhost:8080/api/layers \
  -H 'Content-Type: application/json' \
  -d '{"projectId":1000,"name":"Roads","type":"VECTOR"}'
curl -i http://localhost:8080/api/projects/1000/layers
```

## Suggested time allocation

- 0–4 minutes: read, explain findings, choose priorities.
- 4–22 minutes: implement the most valuable changes.
- 22–27 minutes: add and run focused tests.
- 27–30 minutes: explain tradeoffs and remaining work.
