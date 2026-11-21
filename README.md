# Spring Boot REST API Projects

This repository contains three independent Spring Boot REST API projects:

| # | Project | Purpose | Default Port |
|---|---------|---------|--------------|
| 1 | [Policy Management System](#1-policy-management-system) | Manage insurance policies | `9192` |
| 2 | [E-Commerce Product Management System](#2-e-commerce-product-management-system) | Manage products and inventory | `9193` |
| 3 | [Loan Process Management System](#3-loan-process-management-system) | Manage loans, EMI and approvals | `8181` |

All projects use **Spring Boot**, **Spring Data JPA** and **Jakarta Bean Validation**. Because they use different ports, they can run at the same time.

---

## Table of Contents

- [Common Tech Stack](#common-tech-stack)
- [Repository Structure](#repository-structure)
- [Getting Started (All Projects)](#getting-started-all-projects)
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
- [3. Loan Process Management System](#3-loan-process-management-system)
  - [Project Structure](#loan-project-structure)
  - [Data Model](#loan-data-model)
  - [Validation Rules](#loan-validation-rules)
  - [API Endpoints](#loan-api-endpoints)
  - [Examples](#loan-request-and-response-examples)
  - [Business Rules](#loan-business-rules)
  - [Sample Data (SQL)](#loan-sample-data-sql)
  - [Known Issues](#loan-known-issues-and-improvements)
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
├── loan-process-management/         # Project 3: Loans (port 8181)
│   └── src/main/java/com/loan/management/process/example
├── postman/                         # Postman request collections
└── README.md
```

---

## Getting Started (All Projects)

### Prerequisites

- JDK 17 or higher
- Maven or Gradle
- A database supported by Spring Data JPA (H2, MySQL or PostgreSQL)
- Postman (optional, for testing)

### Required dependencies

Each project needs these starters (plus the MySQL driver if you use MySQL):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

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

**Loan Process Management System** (MySQL)

```properties
spring.application.name=Loan-process-Management
server.port=8181

spring.datasource.name=LoanProcess
spring.datasource.url=jdbc:mysql://localhost:3306/LoanProcess
spring.datasource.username=root
spring.datasource.password=<your-password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Create the `LoanProcess` database first (`CREATE DATABASE IF NOT EXISTS LoanProcess;`). With `ddl-auto=update`, Hibernate creates the `loan` table on startup.
> Do not commit real database passwords. Use an environment variable or a local, git-ignored properties file instead.

For MySQL or PostgreSQL in the other projects, replace the datasource settings with your own URL, username, password and driver.

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
| Loan     | `http://localhost:8181` |

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

# 3. Loan Process Management System

A RESTful backend for managing bank loans. It supports creating, reading, deleting and searching loans, updating the loan status, automatic EMI calculation, and approval rules based on the loan amount.

## Loan Project Structure

```
com.loan.management.process.example
├── LoanProcessManagementApplication.java   # Application entry point
├── controller
│   └── LoanController.java                 # REST endpoints
├── dao
│   └── LoanRepository.java                 # JPA repository
├── dto
│   └── LoanStatusUpdateRequest.java        # Request body for PATCH status
├── entity
│   └── Loan.java                           # JPA entity with validation and EMI calculation
└── exception
    └── GlobalExceptionHandler.java         # Validation error handler
```

## Loan Data Model

**Entity: `Loan`** (table `loan`)

| Field           | Type      | Description                                                   |
|-----------------|-----------|---------------------------------------------------------------|
| `id`            | Long      | Auto-generated primary key (auto-increment)                   |
| `loanNumber`    | String    | Unique loan number (for example `LN1021`)                     |
| `customerName`  | String    | Name of the customer                                          |
| `loanType`      | String    | Type of loan (for example `Home`, `Car`, `Personal`)          |
| `loanAmount`    | Double    | Loan amount                                                   |
| `interestRate`  | Double    | Annual interest rate in percent (5 to 20)                     |
| `tenureMonths`  | Integer   | Loan tenure in months                                         |
| `emiAmount`     | Double    | Monthly EMI, calculated automatically                         |
| `loanStatus`    | String    | `PENDING`, `APPROVED` or `REJECTED`                           |
| `approvalDate`  | LocalDate | Date of approval (`yyyy-MM-dd`), set automatically            |

## Loan Validation Rules

| Field          | Rule                                                      |
|----------------|-----------------------------------------------------------|
| `loanNumber`   | Must not be blank and must be unique                      |
| `customerName` | Must not be blank                                         |
| `loanType`     | Must not be blank                                         |
| `loanAmount`   | Required and greater than 0                               |
| `interestRate` | Required, between 5 and 20                                |
| `tenureMonths` | Required and greater than 0                               |
| `emiAmount`    | Calculated by the server, any client value is overwritten |
| `loanStatus`   | Optional on create (defaults to `PENDING`), must be `PENDING`, `APPROVED` or `REJECTED` |
| `approvalDate` | Set by the server                                         |

**PATCH body (`LoanStatusUpdateRequest`)**

| Field        | Rule                                                      |
|--------------|-----------------------------------------------------------|
| `loanStatus` | Must not be blank, must be `PENDING`, `APPROVED` or `REJECTED` |

## Loan API Endpoints

Base URL: `http://localhost:8181`

| Method | Endpoint                | Description                                  | Success | Error codes |
|--------|-------------------------|----------------------------------------------|---------|-------------|
| POST   | `/loans`                | Create a new loan                            | 201     | 400, 409    |
| GET    | `/loans`                | Get all loans                                | 200     | -           |
| GET    | `/loans/{id}`           | Get a loan by ID                             | 200     | 404         |
| PATCH  | `/loans/{id}/status`    | Update the loan status                       | 200     | 400, 404    |
| DELETE | `/loans/{id}`           | Delete a loan by ID                          | 200     | 404         |
| GET    | `/loans/type/{loanType}`| Find loans by loan type (case-insensitive)   | 200     | -           |
| GET    | `/loans/high-interest`  | Find loans with interest rate greater than 10| 200     | -           |

## Loan Request and Response Examples

### Create a loan

`POST /loans`

```json
{
  "loanNumber": "LN1021",
  "customerName": "Rahul Sharma",
  "loanType": "Home",
  "loanAmount": 500000,
  "interestRate": 10,
  "tenureMonths": 60
}
```

**Response: `201 Created`** (EMI is calculated and status defaults to `PENDING`)

```json
{
  "id": 9,
  "loanNumber": "LN1021",
  "customerName": "Rahul Sharma",
  "loanType": "Home",
  "loanAmount": 500000.0,
  "interestRate": 10.0,
  "tenureMonths": 60,
  "emiAmount": 10623.52,
  "loanStatus": "PENDING",
  "approvalDate": null
}
```

**Response: `409 Conflict`** (duplicate loan number)

```
Loan number already exists: LN1021
```

**Response: `400 Bad Request`** (validation failure)

```json
{
  "interestRate": "interestRate must be between 5 and 20"
}
```

**Response: `400 Bad Request`** (creating an APPROVED loan of 10 Lakhs or more)

```
Loan can be APPROVED only if loanAmount is less than 10 Lakhs
```

### Get all loans

`GET /loans` returns a JSON array of all loans.

### Get a loan by ID

`GET /loans/4` returns the loan, or `404 Not Found` with `Loan not found with id: 4`.

### Update loan status

`PATCH /loans/5/status`

```json
{
  "loanStatus": "APPROVED"
}
```

**Responses**

- `200 OK`: updated loan returned, with `approvalDate` set to today
- `400 Bad Request`: invalid status value, or approval attempted for a loan of 10 Lakhs or more
- `404 Not Found`: `Loan not found with id: 5`

### Delete a loan by ID

`DELETE /loans/9`

- `200 OK`: `Loan deleted successfully with id: 9`
- `404 Not Found`: `Loan not found with id: 9`

### Find by loan type

`GET /loans/type/home` returns all loans of that type. The match ignores case.

### Find loans with interest rate above 10

`GET /loans/high-interest` returns all loans with an interest rate strictly greater than 10 (a rate of exactly 10 is excluded).

## Loan Business Rules

1. **Unique loan numbers:** creating a loan with an existing number returns `409 Conflict`.
2. **EMI is auto-calculated:** on every create and update, using
   `EMI = P x r x (1 + r)^n / ((1 + r)^n - 1)`, where `P` is the loan amount, `r` is the annual rate divided by 12 and by 100, and `n` is `tenureMonths`. The result is rounded to 2 decimals.
3. **Approval limit:** a loan can be `APPROVED` only if `loanAmount` is less than 10 Lakhs (1,000,000). A loan of exactly 10 Lakhs cannot be approved.
4. **Approval date:** set to today's date when a loan becomes `APPROVED`, and cleared when the status is `PENDING` or `REJECTED`.
5. **Interest rate range:** `interestRate` must be between 5 and 20 (inclusive).
6. **Default status:** a loan created without `loanStatus` is saved as `PENDING`.
7. **ID is server-generated:** any `id` sent in a `POST` body is ignored.
8. **Loan type search is case-insensitive:** `home` and `Home` return the same results.

## Loan Sample Data (SQL)

Rows inserted with raw SQL skip the Java `@PrePersist` callback, so `emi_amount` stays `NULL` until it is calculated. Use the same formula in MySQL:

```sql
CREATE DATABASE IF NOT EXISTS LoanProcess;
USE LoanProcess;

INSERT INTO loan
    (loan_number, customer_name, loan_type, loan_amount, interest_rate, tenure_months, loan_status, approval_date)
VALUES
    ('LN1001', 'Rahul Sharma', 'Home',      500000,  10.0,  60, 'APPROVED', '2026-10-05'),
    ('LN1002', 'Anita Patel',  'Personal',  200000,  14.5,  36, 'APPROVED', '2026-10-12'),
    ('LN1003', 'Vikram Singh', 'Car',       750000,   9.0,  48, 'APPROVED', '2026-09-20'),
    ('LN1004', 'Meera Desai',  'Home',     2500000,   8.5, 240, 'PENDING',  NULL),
    ('LN1005', 'Kiran Mehta',  'Education', 400000,  11.5,  84, 'PENDING',  NULL),
    ('LN1006', 'Arjun Joshi',  'Personal',  150000,  18.0,  24, 'REJECTED', NULL),
    ('LN1007', 'Sneha Verma',  'Business', 1800000,  12.0, 120, 'PENDING',  NULL),
    ('LN1008', 'Kavita Nair',  'Car',       600000,   7.5,  60, 'APPROVED', '2026-11-02');

-- "WHERE id > 0" satisfies MySQL Workbench safe update mode (error 1175)
UPDATE loan
SET emi_amount = ROUND(
        loan_amount * (interest_rate / 1200) * POW(1 + interest_rate / 1200, tenure_months)
        / (POW(1 + interest_rate / 1200, tenure_months) - 1), 2)
WHERE id > 0;
```

> Run the `INSERT` only once. Running it again fails on the unique `loan_number` column.

## Loan Known Issues and Improvements

- **Postman "Update Loan Status" uses the wrong method.** The request saved as "Update Loan Status" is a `GET /loans/4` with a JSON body, so it does not update anything. The working request is "Update Loan Status-1" (`PATCH /loans/5/status`). Rename it and delete the broken one.
- **Postman PATCH sends a full loan body.** Only `loanStatus` is read, and extra fields are ignored. Send just `{ "loanStatus": "APPROVED" }`.
- **Hardcoded database password** in `application.properties`. Move it to an environment variable or a git-ignored file before sharing the repository.
- **`spring.jpa.properties.hibernate.dialect` is redundant.** Hibernate 6 detects the MySQL dialect automatically, so the line can be removed.
- **No bulk delete.** The required API list has no "delete all loans" endpoint, so only `DELETE /loans/{id}` exists.
- **Status changes are unrestricted.** An APPROVED loan can be moved back to `PENDING` or `REJECTED`. Consider adding rules for allowed status transitions.
- **Naming:** the Postman request "Loan By InterestRate" actually calls `/loans/high-interest`. Consider renaming it to "High Interest Loans".
- **Consider adding** a `@Service` layer, pagination for list endpoints, and handling for malformed JSON and invalid dates.

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

### Loan requests (port 8181)

| Request              | Method | URL                                       | Body                              |
|----------------------|--------|-------------------------------------------|-----------------------------------|
| Create Loans         | POST   | `http://localhost:8181/loans`             | JSON (loan fields, see above)     |
| Get Loans            | GET    | `http://localhost:8181/loans`             | none                              |
| Loan By Id           | GET    | `http://localhost:8181/loans/4`           | none                              |
| Update Loan Status   | PATCH  | `http://localhost:8181/loans/5/status`    | `{ "loanStatus": "APPROVED" }`    |
| Delete Loan By Id    | DELETE | `http://localhost:8181/loans/9`           | none                              |
| Loan Type            | GET    | `http://localhost:8181/loans/type/home`   | none                              |
| Loan By InterestRate | GET    | `http://localhost:8181/loans/high-interest` | none                            |

**Loan rule checks**

| Test | Expected result |
|------|-----------------|
| `POST /loans` with `"interestRate": 4` or `25` | `400` interestRate must be between 5 and 20 |
| `POST /loans` with the same `loanNumber` twice | `409` |
| `PATCH /loans/4/status` with `APPROVED` (25 Lakh loan) | `400` approval only below 10 Lakhs |
| `PATCH /loans/5/status` with `APPROVED` (4 Lakh loan) | `200`, `approvalDate` set to today |
| `PATCH /loans/5/status` with `"DONE"` | `400` loanStatus must be PENDING, APPROVED or REJECTED |
| `GET /loans/type/HOME` | Same results as `/loans/type/home` |

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
