# Ecommerce Backend - IN DEVELOPMENT

A Spring Boot REST API for a multi-role e-commerce platform. Supports **Admin**, **Seller**, and **User** roles with JWT authentication, rate limiting, image uploads, and structured JSON logging compatible with the ELK stack (Elasticsearch + Logstash + Kibana).

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Rate Limiting](#rate-limiting)
- [Logging & ELK Stack](#logging--elk-stack)
- [Environment Configuration](#environment-configuration)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.x |
| Security | Spring Security + JWT (JJWT 0.12.6) |
| Database | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Mapping | MapStruct 1.6.2 |
| Logging | Logback + logstash-logback-encoder 8.0 |
| Build | Maven |

---

## Project Structure

```
src/main/java/com/rehancode/ecommercebackend/
├── Config/             # File upload configuration
├── Controller/         # REST controllers (Auth, Admin, Seller, User, Public)
├── DTO/                # Request and response data transfer objects
├── Enum/               # Roles, STATUS, ProductStatus enums
├── Exception/          # Custom exceptions and global exception handler
├── Jwt/                # JWT filter and token service
├── Logging/            # RequestLoggingFilter (MDC population + request tracing)
├── Mapper/             # MapStruct mappers
├── Model/              # JPA entities (UserModel, Products, Category)
├── RateLimiting/       # Per-endpoint rate limiter (filter, rules, config)
├── Repository/         # Spring Data JPA repositories
├── Security/           # SecurityConfig, CustomUserDetailsService
├── Service/            # Business logic (Auth, Admin, Seller, User, Public)
└── Utils/              # SecurityUtils for authenticated user context
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8
- Docker + Docker Compose (for ELK stack)

### Setup

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd Ecommerce-backend
   ```

2. **Create the MySQL database**
   ```sql
   CREATE DATABASE Ecommerce_Backend;
   ```

3. **Configure credentials**  
   Edit `src/main/resources/application.properties` — set `spring.datasource.username`, `spring.datasource.password`, and `JWT_SECRET`.

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The server starts at `http://localhost:8080`.

---

## API Endpoints

### Auth — `/api/auth` (Public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT token |

### Public — `/api/public` (No auth required)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/public/getProduct` | List all products (paginated) |
| POST | `/api/public/getProduct/{id}` | Get product by ID |

### User — `/api/users` (Requires JWT)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/me` | Get own profile |
| PUT | `/api/users/update` | Update profile |
| POST | `/api/users/change-password` | Change password |
| POST | `/api/users/getCategory` | List categories (paginated) |
| POST | `/api/users/getCategory/{id}` | Get category by ID |

### Seller — `/api/seller` (Requires `ROLE_SELLER`)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/seller/addProduct` | Add a new product with image upload |
| POST | `/api/seller/getProduct` | List own products (paginated) |
| POST | `/api/seller/getProduct/{id}` | Get own product by ID |

### Admin — `/api/admin` (Requires `ROLE_ADMIN`)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/admin/addCategory` | Create a new category |
| PUT | `/api/admin/updateCategory/{id}` | Update a category |
| POST | `/api/admin/registerSeller` | Create a seller account |
| POST | `/api/admin/getSellers` | List all sellers (paginated) |
| POST | `/api/admin/getSeller/{id}` | Get seller by ID |
| POST | `/api/admin/blockSeller/{id}` | Block a seller |
| POST | `/api/admin/unblockSeller/{id}` | Unblock a seller |
| POST | `/api/admin/deleteSeller/{id}` | Delete a seller |

---

## Authentication

All protected endpoints require a `Bearer` token in the `Authorization` header:

```
Authorization: Bearer <your-jwt-token>
```

Obtain a token by calling `POST /api/auth/login` with your credentials.

---

## Rate Limiting

Rate limits are applied per endpoint group:

| Path prefix | Limit |
|---|---|
| `/api/auth/**` | 5 requests / minute (per IP) |
| `/api/public/**` | 30 requests / minute (per IP) |
| `/api/admin/**` | 50 requests / minute (per user ID) |
| `/api/seller/**` | 50 requests / minute (per user ID) |

When the limit is exceeded the API returns `429 Too Many Requests`.

---

## Logging & ELK Stack

### Overview

Every log line is emitted as **structured JSON** using [logstash-logback-encoder](https://github.com/logfellow/logstash-logback-encoder). This makes logs directly indexable by Elasticsearch without custom parsing rules in Logstash.

### Log Output Sample

```json
{
  "timestamp": "2024-07-20T14:32:11.456Z",
  "@version": "1",
  "message": "Request completed",
  "logger": "c.r.e.Logging.RequestLoggingFilter",
  "level": "INFO",
  "requestId": "a3f9d2b1-...",
  "ip": "127.0.0.1",
  "method": "POST",
  "path": "/api/auth/login",
  "statusCode": "200",
  "duration": "42ms",
  "username": "john@example.com",
  "userAgent": "PostmanRuntime/7.36.0"
}
```

### MDC Fields

The `RequestLoggingFilter` populates these fields automatically on every request. They are available on every log line written during request processing:

| Field | Description |
|---|---|
| `requestId` | Unique UUID per request — also sent as `X-Request-Id` response header |
| `ip` | Client IP (proxy-aware via `X-Forwarded-For`) |
| `method` | HTTP method |
| `path` | Request URI |
| `userAgent` | Caller's User-Agent header |
| `username` | Authenticated user (or `anonymous`) |
| `statusCode` | HTTP response status |
| `duration` | Request duration in ms |

### Log Files

Logs are written to `logs/ecommerce-backend.log` with daily rolling and 30-day retention. You can point Filebeat or any log shipper at this file to push logs into Elasticsearch.

---

## Environment Configuration

| Property | Description | Default |
|---|---|---|
| `server.port` | HTTP port | `8080` |
| `spring.datasource.url` | MySQL JDBC URL | `localhost:3306/Ecommerce_Backend` |
| `spring.datasource.username` | DB username | `root` |
| `spring.datasource.password` | DB password | — |
| `JWT_SECRET` | JWT signing secret (hex string) | — |
| `logging.file.name` | Log file path | `logs/ecommerce-backend.log` |
| `logging.level.root` | Root log level | `INFO` |

For `dev` profile: application log level is set to `DEBUG`.  
For `prod` profile: Spring and Hibernate logs are suppressed to `WARN`.
