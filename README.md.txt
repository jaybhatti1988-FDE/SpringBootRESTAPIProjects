# Policy Management System

A RESTful backend for managing insurance policies, built with **Spring Boot**, **Spring Data JPA** and **Jakarta Bean Validation**. It supports creating, reading, updating and deleting policies, with business rules such as unique policy numbers and protection of ACTIVE policies from deletion.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Data Model](#data-model)
- [Validation Rules](#validation-rules)
- [API Endpoints](#api-endpoints)
- [Request and Response Examples](#request-and-response-examples)
- [Business Rules](#business-rules)
- [Testing with Postman](#testing-with-postman)
- [Known Issues and Improvements](#known-issues-and-improvements)

---

## Tech Stack

| Component        | Technology                          |
|------------------|-------------------------------------|
| Language         | Java 17+                            |
| Framework        | Spring Boot 3.x (Jakarta namespace) |
| Persistence      | Spring Data JPA (Hibernate)         |
| Validation       | Jakarta Bean Validation             |
| Build Tool       | Maven or Gradle                     |
| API Testing      | Postman                             |

> Note: the code uses `jakarta.*` imports, which requires Spring Boot 3 or newer and Java 17 or newer.

---

## Project Structure

```
com.insurancepolicy.policymgmtsystem.example
├── SpringbootpolicymgmtsystemApplication.java   # Application entry point
├── controller
│   └── InsurancePolicyController.java           # REST endpoints
├── dao
│   └── InsurancePolicyRepository.java           # JPA repository
├── entity
│   └── InsurancePolicy.java                     # JPA entity with validation
└── exception
    └── GlobalExceptionHandler.java              # Validation error handler
```

---

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven or Gradle
- A database supported by Spring Data JPA (for example H2, MySQL or PostgreSQL)
- Postman (optional, for testing)

### Configuration

Create or update `src/main/resources/application.properties`. The Postman collection expects the app on port **9192**:

```properties
server.port=9192

# Example: H2 in-memory database
spring.datasource.url=jdbc:h2:mem:policydb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

For MySQL, replace the datasource settings with your own URL, username, password and driver.

### Run the application

```bash
# Maven
mvn spring-boot:run

# Gradle
./gradlew bootRun
```

The API will be available at `http://localhost:9192`.

---

## Data Model

**Entity: `InsurancePolicy`**

| Field            | Type      | Description                                   |
|------------------|-----------|-----------------------------------------------|
| `id`             | Long      | Auto-generated primary key                    |
| `policyNumber`   | String    | Unique policy number (for example `POL1007`)  |
| `policyHolderName` | String  | Name of the policy holder                     |
| `policyType`     | String    | Type of policy (for example `Health`)         |
| `premiumAmount`  | Double    | Premium amount                                |
| `coverageAmount` | Double    | Coverage amount                               |
| `startDate`      | LocalDate | Policy start date (`yyyy-MM-dd`)              |
| `endDate`        | LocalDate | Policy end date (`yyyy-MM-dd`)                |
| `nomineeName`    | String    | Nominee name                                  |
| `policyStatus`   | String    | `ACTIVE` or `INACTIVE`                        |

---

## Validation Rules

| Field              | Rule                                              |
|--------------------|---------------------------------------------------|
| `policyNumber`     | Must be unique (database constraint and API check) |
| `policyHolderName` | Must not be blank                                 |
| `policyType`       | Must not be blank                                 |
| `premiumAmount`    | Required and greater than 0                       |
| `coverageAmount`   | Required and greater than 0                       |
| `startDate`        | Required                                          |
| `endDate`          | Required, and must be after `startDate`           |
| `nomineeName`      | Must not be blank                                 |
| `policyStatus`     | Must be `ACTIVE` or `INACTIVE`                    |

---

## API Endpoints

Base URL: `http://localhost:9192`

| Method | Endpoint          | Description                          | Success | Error codes        |
|--------|-------------------|--------------------------------------|---------|--------------------|
| GET    | `/policies`       | Get all policies                     | 200     | -                  |
| GET    | `/policies/{id}`  | Get a policy by ID                   | 200     | 404                |
| POST   | `/policies`       | Create a new policy                  | 201     | 400, 409           |
| PUT    | `/policies/{id}`  | Update an existing policy            | 200     | 400, 404, 409      |
| DELETE | `/policies/{id}`  | Delete a policy by ID (INACTIVE only)| 200     | 404, 409           |
| DELETE | `/policies`       | Delete all INACTIVE policies         | 200     | -                  |

---

## Request and Response Examples

### Create a policy

`POST /policies`

```json
{
  "policyNumber": "POL1007",
  "policyHolderName": "Radha Verma",
  "policyType": "Health",
  "premiumAmount": 9000,
  "coverageAmount": 750000,
  "startDate": "2026-11-18",
  "endDate": "2027-11-17",
  "nomineeName": "Sita Verma",
  "policyStatus": "ACTIVE"
}
```

**Response: `201 Created`**

```json
{
  "id": 1,
  "policyNumber": "POL1007",
  "policyHolderName": "Radha Verma",
  "policyType": "Health",
  "premiumAmount": 9000.0,
  "coverageAmount": 750000.0,
  "startDate": "2026-11-18",
  "endDate": "2027-11-17",
  "nomineeName": "Sita Verma",
  "policyStatus": "ACTIVE"
}
```

**Response: `409 Conflict`** (duplicate policy number)

```
Policy number already exists: POL1007
```

### Get all policies

`GET /policies` returns a JSON array of all policies.

### Get a policy by ID

`GET /policies/52` returns the policy, or `404 Not Found` if it does not exist.

### Update a policy

`PUT /policies/52`

```json
{
  "policyNumber": "POL1002",
  "policyHolderName": "Anita Patel",
  "policyType": "Health",
  "premiumAmount": 9500,
  "coverageAmount": 600000,
  "startDate": "2025-06-14",
  "endDate": "2027-06-14",
  "nomineeName": "Rajesh Patel",
  "policyStatus": "ACTIVE"
}
```

**Responses**

- `200 OK`: updated policy returned
- `404 Not Found`: `Policy not found with id: 52`
- `409 Conflict`: `Policy number already exists: POL1002` (used by another policy)

### Delete a policy by ID

`DELETE /policies/{id}`

- `200 OK`: `Policy deleted successfully with id: 1`
- `409 Conflict`: `Cannot delete ACTIVE policy with id: 1`
- `404 Not Found`: `Policy not found with id: 1`

### Delete all INACTIVE policies

`DELETE /policies`

```
Deleted 3 Inactive Policies. 5 ACTIVE policies were not deleted
```

---

## Business Rules

1. **Unique policy numbers:** creating or updating a policy with a number already used by another policy returns `409 Conflict`.
2. **Active policies are protected:** an ACTIVE policy cannot be deleted. Set its status to `INACTIVE` first using `PUT /policies/{id}`.
3. **Bulk delete is safe:** `DELETE /policies` removes only INACTIVE policies and reports how many ACTIVE policies were kept.
4. **ID is server-generated:** any `id` sent in a `POST` body is ignored.
5. **Date check:** `endDate` must be after `startDate`.

---

## Testing with Postman

The following requests are included in the Postman collection:

| Request              | Method | URL                                  |
|----------------------|--------|--------------------------------------|
| Create Policy        | POST   | `http://localhost:9192/policies`     |
| Get Policies         | GET    | `http://localhost:9192/policies`     |
| Get Policies By ID   | GET    | `http://localhost:9192/policies/52`  |
| Update Policy        | PUT    | `http://localhost:9192/policies/52`  |
| Delete Policy By Id  | DELETE | `http://localhost:9192/policies/1`   |
| Delete Policy        | DELETE | `http://localhost:9192/policies`     |

**Tips**

- Set the `Content-Type` header to `application/json` for POST and PUT requests.
- DELETE requests do not need a body.
- Replace the IDs in the URLs with IDs that exist in your database.

---

## Known Issues and Improvements

- **Validation errors may not return the custom JSON format.** `GlobalExceptionHandler` is missing the `@RestControllerAdvice` annotation, so Spring does not register it. Add the annotation to return field-level errors as JSON with `400 Bad Request`:

  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler { ... }
  ```

- **Port mismatch in Postman:** the "Get Policies" request points to port `9193`, while all others use `9192`. Change it to `9192`.
- **Request naming:** the PUT request is saved as "Get Policies By ID-1". Consider renaming it to "Update Policy".
- **`policyNumber` is optional** in the entity (no `@NotBlank`). Add `@NotBlank` if every policy must have a number.
- **`policyStatus` is optional**, and a null status is allowed. Add `@NotNull` if it is required.
- **Replace `System.out.println`** with a logger such as SLF4J.
- **Use `LocalDate.now()` rules** if you want to prevent policies with start dates in the past.
- Consider adding pagination to `GET /policies`, a service layer, unit and integration tests, and OpenAPI/Swagger documentation.

---

## License

This project is for learning and demonstration purposes.