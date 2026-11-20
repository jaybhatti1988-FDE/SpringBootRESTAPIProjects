# Spring Boot REST API Projects

This repository contains two independent Spring Boot REST API projects:

| # | Project | Purpose | Default Port |
|---|---------|---------|--------------|
| 1 | [Policy Management System](#1-policy-management-system) | Manage insurance policies | `9192` |
| 2 | [E-Commerce Product Management System](#2-e-commerce-product-management-system) | Manage products and inventory | `9193` |

Both projects use **Spring Boot**, **Spring Data JPA** and **Jakarta Bean Validation**. Because they use different ports, they can run at the same time.

---

## Table of Contents

- [Common Tech Stack](#common-tech-stack)
- [Repository Structure](#repository-structure)
- [Getting Started (Both Projects)](#getting-started-both-projects)
- [1. Policy Management System](#1-policy-management-system)
  - [Project Structure](#policy-project-structure)
  - [Data Model](#policy-data-model)
  - [Validation Rules](#policy-validation-rules)
  - [API Endpoints](#policy-api-endpoints)
  - [Examples](#policy-request-and-response-examples)
  - [Business Rules](#policy-business-rules)
  - [Known Issues](#policy-known-issues-and-improvements)
- [2. E-Commerce Product Management System](#2-e-commerce-product-management-system)
  - [Project Structure](#product-project-structure)
  - [Data Model](#product-data-model)
  - [Validation Rules](#product-validation-rules)
  - [API Endpoints](#product-api-endpoints)
  - [Examples](#product-request-and-response-examples)
  - [Business Rules](#product-business-rules)
  - [Known Issues](#product-known-issues-and-improvements)
- [Testing with Postman](#testing-with-postman)
- [Future Improvements](#future-improvements)
- [License](#license)

---

## Common Tech Stack

| Component        | Technology                          |
|------------------|-------------------------------------|
| Language         | Java 17+                            |
| Framework        | Spring Boot 3.x (Jakarta namespace) |
| Persistence      | Spring Data JPA (Hibernate)         |
| Validation       | Jakarta Bean Validation             |
| Build Tool       | Maven or Gradle                     |
| API Testing      | Postman                             |

> The code uses `jakarta.*` imports, which requires Spring Boot 3 or newer and Java 17 or newer.

---

## Repository Structure

Adjust the folder names to match your repository.

```
.
├── policy-management-system/        # Project 1: Insurance policies (port 9192)
│   └── src/main/java/com/insurancepolicy/policymgmtsystem/example
├── product-management-system/       # Project 2: E-commerce products (port 9193)
│   └── src/main/java/com/ecommrce/product/mgmt/system/example
├── postman/                         # Postman request collections
└── README.md
```

---

## Getting Started (Both Projects)

### Prerequisites

- JDK 17 or higher
- Maven or Gradle
- A database supported by Spring Data JPA (H2, MySQL or PostgreSQL)
- Postman (optional, for testing)

### Configuration

Each project has its own `src/main/resources/application.properties`.

**Policy Management System**

```properties
server.port=9192

spring.datasource.url=jdbc:h2:mem:policydb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

**E-Commerce Product Management System**

```properties
server.port=9193

spring.datasource.url=jdbc:h2:mem:productdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

For MySQL or PostgreSQL, replace the datasource settings with your own URL, username, password and driver.

### Run a project

Open a terminal in the project folder and run:

```bash
# Maven
mvn spring-boot:run

# Gradle
./gradlew bootRun
```

| Project  | Base URL                |
|----------|-------------------------|
| Policy   | `http://localhost:9192` |
| Product  | `http://localhost:9193` |

---

# 1. Policy Management System

A RESTful backend for managing insurance policies. It supports creating, reading, updating and deleting policies, with business rules such as unique policy numbers and protection of ACTIVE policies from deletion.

## Policy Project Structure

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

## Policy Data Model

**Entity: `InsurancePolicy`**

| Field              | Type      | Description                                  |
|--------------------|-----------|----------------------------------------------|
| `id`               | Long      | Auto-generated primary key                   |
| `policyNumber`     | String    | Unique policy number (for example `POL1007`) |
| `policyHolderName` | String    | Name of the policy holder                    |
| `policyType`       | String    | Type of policy (for example `Health`)        |
| `premiumAmount`    | Double    | Premium amount                               |
| `coverageAmount`   | Double    | Coverage amount                              |
| `startDate`        | LocalDate | Policy start date (`yyyy-MM-dd`)             |
| `endDate`          | LocalDate | Policy end date (`yyyy-MM-dd`)               |
| `nomineeName`      | String    | Nominee name                                 |
| `policyStatus`     | String    | `ACTIVE` or `INACTIVE`                       |

## Policy Validation Rules

| Field              | Rule                                               |
|--------------------|----------------------------------------------------|
| `policyNumber`     | Must be unique (database constraint and API check) |
| `policyHolderName` | Must not be blank                                  |
| `policyType`       | Must not be blank                                  |
| `premiumAmount`    | Required and greater than 0                        |
| `coverageAmount`   | Required and greater than 0                        |
| `startDate`        | Required                                           |
| `endDate`          | Required, and must be after `startDate`            |
| `nomineeName`      | Must not be blank                                  |
| `policyStatus`     | Must be `ACTIVE` or `INACTIVE`                     |

## Policy API Endpoints

Base URL: `http://localhost:9192`

| Method | Endpoint         | Description                           | Success | Error codes   |
|--------|------------------|---------------------------------------|---------|---------------|
| GET    | `/policies`      | Get all policies                      | 200     | -             |
| GET    | `/policies/{id}` | Get a policy by ID                    | 200     | 404           |
| POST   | `/policies`      | Create a new policy                   | 201     | 400, 409      |
| PUT    | `/policies/{id}` | Update an existing policy             | 200     | 400, 404, 409 |
| DELETE | `/policies/{id}` | Delete a policy by ID (INACTIVE only) | 200     | 404, 409      |
| DELETE | `/policies`      | Delete all INACTIVE policies          | 200     | -             |

## Policy Request and Response Examples

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

## Policy Business Rules

1. **Unique policy numbers:** creating or updating a policy with a number already used by another policy returns `409 Conflict`.
2. **Active policies are protected:** an ACTIVE policy cannot be deleted. Set its status to `INACTIVE` first using `PUT /policies/{id}`.
3. **Bulk delete is safe:** `DELETE /policies` removes only INACTIVE policies and reports how many ACTIVE policies were kept.
4. **ID is server-generated:** any `id` sent in a `POST` body is ignored.
5. **Date check:** `endDate` must be after `startDate`.

## Policy Known Issues and Improvements

- **`GlobalExceptionHandler` is missing `@RestControllerAdvice`**, so Spring does not register it and validation errors will not return the custom JSON. Add the annotation:

  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler { ... }
  ```

- **Port mismatch in Postman:** the "Get Policies" request points to port `9193` (the Product project's port) instead of `9192`.
- **Request naming:** the PUT request is saved as "Get Policies By ID-1". Consider renaming it to "Update Policy".
- **`policyNumber` and `policyStatus` are optional** in the entity. Add `@NotBlank` / `@NotNull` if they are required.

---

# 2. E-Commerce Product Management System

A RESTful backend for managing an e-commerce product catalog and inventory. It supports creating, reading, deleting and searching products, partial updates of price and stock, and automatic availability status based on stock.

## Product Project Structure

```
com.ecommrce.product.mgmt.system.example
├── ECommerceProductManagementSystemApplication.java   # Application entry point
├── controller
│   └── ProductController.java                         # REST endpoints
├── dao
│   └── productInventoryRepository.java                # JPA repository
├── dto
│   └── PriceStockUpdateRequest.java                   # Request body for PATCH
├── entity
│   └── Product.java                                   # JPA entity with validation
└── exception
    └── GlobalExceptionHandler.java                    # Validation error handler
```

## Product Data Model

**Entity: `Product`**

| Field                | Type    | Description                                          |
|----------------------|---------|------------------------------------------------------|
| `id`                 | Long    | Auto-generated primary key                           |
| `productName`        | String  | Name of the product                                  |
| `productCode`        | String  | Unique product code (for example `PRD2351`)          |
| `category`           | String  | Product category (for example `Electronics`)         |
| `price`              | Double  | Product price                                        |
| `stockQuantity`      | Integer | Units in stock                                       |
| `brand`              | String  | Brand name                                           |
| `rating`             | Double  | Rating from 0.0 to 5.0 (optional)                    |
| `description`        | String  | Description, up to 1000 characters (optional)        |
| `availabilityStatus` | String  | `IN_STOCK` or `OUT_OF_STOCK`, set automatically      |

## Product Validation Rules

| Field                | Rule                                                  |
|----------------------|-------------------------------------------------------|
| `productName`        | Must not be blank                                     |
| `productCode`        | Must not be blank and must be unique                  |
| `category`           | Must not be blank                                     |
| `price`              | Required and greater than 0                           |
| `stockQuantity`      | Required and 0 or more                                |
| `brand`              | Must not be blank                                     |
| `rating`             | Optional, between 0.0 and 5.0                         |
| `description`        | Optional, maximum 1000 characters                     |
| `availabilityStatus` | Calculated by the server, any client value is overwritten |

**PATCH body (`PriceStockUpdateRequest`)**

| Field           | Rule                           |
|-----------------|--------------------------------|
| `price`         | Must be positive               |
| `stockQuantity` | Must be 0 or more              |

## Product API Endpoints

Base URL: `http://localhost:9193`

| Method | Endpoint                                | Description                          | Success | Error codes |
|--------|-----------------------------------------|--------------------------------------|---------|-------------|
| POST   | `/products`                             | Create a new product                 | 201     | 400, 409    |
| GET    | `/products`                             | Get all products                     | 200     | -           |
| GET    | `/products/{id}`                        | Get a product by ID                  | 200     | 404         |
| PATCH  | `/products/{id}`                        | Update price and stock only          | 200     | 400, 404    |
| DELETE | `/products/{id}`                        | Delete a product by ID               | 200     | 404         |
| DELETE | `/products`                             | Delete all products                  | 200     | -           |
| GET    | `/products/category/{category}`         | Find products by category (case-insensitive) | 200 | -        |
| GET    | `/products/price-greater-than?price=X`  | Find products priced above X         | 200     | -           |

## Product Request and Response Examples

### Create a product

`POST /products`

```json
{
  "productName": "Wireless Color Printer",
  "productCode": "PRD2351",
  "category": "Electronics",
  "price": 5699,
  "stockQuantity": 0,
  "brand": "BPCL",
  "rating": 4.7,
  "description": "Bluetooth wireless sensor Color Printer"
}
```

**Response: `201 Created`** (note the automatic `OUT_OF_STOCK` status because stock is 0)

```json
{
  "id": 1,
  "productName": "Wireless Color Printer",
  "productCode": "PRD2351",
  "category": "Electronics",
  "price": 5699.0,
  "stockQuantity": 0,
  "brand": "BPCL",
  "rating": 4.7,
  "description": "Bluetooth wireless sensor Color Printer",
  "availabilityStatus": "OUT_OF_STOCK"
}
```

**Response: `409 Conflict`** (duplicate product code)

```
 Product code already exists: PRD2351
```

**Response: `400 Bad Request`** (validation failure; requires `@RestControllerAdvice`, which this project has)

```json
{
  "price": "must be greater than 0",
  "productName": "must not be blank"
}
```

### Get all products

`GET /products` returns a JSON array of all products.

### Get a product by ID

`GET /products/7` returns the product, or `404 Not Found` with `Product Not Found with Id 7`.

### Update price and stock

`PATCH /products/6`

```json
{
  "price": 1299,
  "stockQuantity": 425
}
```

**Responses**

- `200 OK`: updated product returned (availability status recalculated)
- `400 Bad Request`: price is not positive or stock is negative
- `404 Not Found`: `Product not found with id: 6`

### Delete a product by ID

`DELETE /products/7`

- `200 OK`: `Product deleted successfully with id: 7`
- `404 Not Found`: `Product not found with id: 7`

### Delete all products

`DELETE /products`

```
All products deleted successfully
```

### Find by category

`GET /products/category/Smartphones` returns all products in that category. The match ignores case.

### Find by minimum price

`GET /products/price-greater-than?price=10000` returns all products with a price above 10000.

## Product Business Rules

1. **Unique product codes:** creating a product with an existing code returns `409 Conflict`.
2. **Automatic availability:** `availabilityStatus` is `OUT_OF_STOCK` when `stockQuantity` is 0, otherwise `IN_STOCK`. It is recalculated on every create and update.
3. **PATCH updates only price and stock:** other fields in the request body are ignored.
4. **ID is server-generated:** any `id` sent in a `POST` body is ignored.
5. **Category search is case-insensitive:** `electronics` and `Electronics` return the same results.

## Product Known Issues and Improvements

- **Wrong `@NonNull` import in `PriceStockUpdateRequest`.** It uses `io.micrometer.common.lang.NonNull`, which does not validate requests. If `price` or `stockQuantity` is missing, the request can fail with a `500` error instead of `400`. Replace it with `jakarta.validation.constraints.NotNull`.
- **Postman "Update Product" sends a full product body** to the PATCH endpoint. Only `price` and `stockQuantity` are used, so you can send just those two fields.
- **`DELETE /products` removes every product** with no protection. Consider restricting it or adding a confirmation step.
- **Naming:** the repository class `productInventoryRepository` should start with an uppercase letter (`ProductInventoryRepository`), the controller method `getpolicies()` should be renamed to `getProducts()`, and the package name `ecommrce` looks like a typo of `ecommerce`.
- **Consider adding** `@RestControllerAdvice` handling for other errors, a full `PUT` endpoint, and pagination for list endpoints.

---

## Testing with Postman

### Policy requests (port 9192)

| Request              | Method | URL                                 |
|----------------------|--------|-------------------------------------|
| Create Policy        | POST   | `http://localhost:9192/policies`    |
| Get Policies         | GET    | `http://localhost:9192/policies`    |
| Get Policies By ID   | GET    | `http://localhost:9192/policies/52` |
| Update Policy        | PUT    | `http://localhost:9192/policies/52` |
| Delete Policy By Id  | DELETE | `http://localhost:9192/policies/1`  |
| Delete Policy        | DELETE | `http://localhost:9192/policies`    |

### Product requests (port 9193)

| Request                          | Method | URL                                                         |
|----------------------------------|--------|-------------------------------------------------------------|
| Create Product                   | POST   | `http://localhost:9193/products`                            |
| Get Products                     | GET    | `http://localhost:9193/products`                            |
| Get Products By Id               | GET    | `http://localhost:9193/products/7`                          |
| Update Product                   | PATCH  | `http://localhost:9193/products/6`                          |
| Delete Products By Id            | DELETE | `http://localhost:9193/products/7`                          |
| Delete Products                  | DELETE | `http://localhost:9193/products`                            |
| Get Products By Category         | GET    | `http://localhost:9193/products/category/Smartphones`       |
| Get Products By Price greater than | GET  | `http://localhost:9193/products/price-greater-than?price=10000` |

**Tips**

- Set the `Content-Type` header to `application/json` for POST, PUT and PATCH requests.
- DELETE and GET requests do not need a body.
- Replace the IDs in the URLs with IDs that exist in your database.
- Make sure the correct project is running on the port you are calling.

---

## Future Improvements

- Add a service layer between controllers and repositories
- Replace `System.out.println` with a logger such as SLF4J
- Add pagination and sorting to list endpoints
- Add unit and integration tests
- Add OpenAPI/Swagger documentation
- Add authentication and authorization (Spring Security)

---

## License

These projects are for learning and demonstration purposes.
