# Blogging Platform API

Robust RESTful API built with **Java, Spring Boot, and PostgreSQL**, featuring JWT authentication, pagination, custom exception handling, and fully documented endpoints using Swagger (OpenAPI), following best practices and layered arquitecture with clear separation of concerns.
The API allows users to create and manage blog posts while implementing authentication and role-based authorization using **Spring Security**.

---

## Quick Start

1. Clone the repository

   git clone https://github.com/lautarorisso/blogging-api.git

2. Run the application

```Linux / macOS
./mvnw spring-boot:run
cd blogging-api
```

```Windows
mvnw.cmd spring-boot:run
cd blogging-api
```

or run the main class from your IDE.

✅ The API will start at:
http://localhost:8080

---

## API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui.html

or

http://localhost:8080/swagger-ui/index.html

---

## Environment Variables (Optional)

The application includes default values, so you can run it without configuration.

If you want to customize settings, you can define environment variables or create a `.env` file based on the provided example:

```bash
cp .env.example .env
```

```Windows
copy .env.example .env
```

Then edit the values as needed.

| Variable    | Description       | Default                                  |
| ----------- | ----------------- | ---------------------------------------- |
| DB_URL      | Database URL      | jdbc:postgresql://localhost:5432/blog_db |
| DB_USER     | Database username | postgres                                 |
| DB_PASSWORD | Database password | postgres                                 |
| JWT_SECRET  | JWT secret key    | my-secret-key                            |

Example (PowerShell):

```Windows
  $env:DB_PASSWORD="mypassword"
  mvnw.cmd spring-boot:run
```

---

## Features

- User registration and login
- Role-based authorization (USER / ADMIN)
- Create a blog post
- Update an existing blog post
- Delete a blog post
- Retrieve a single blog post
- Retrieve all blog posts
- Filter blog posts by a search term
- API documentation with Swagger / OpenAPI
- Unit tests with JUnit and Mockito
- Request validation with Jakarta Validation

---

## Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger / OpenAPI**
- **JUnit / Mockito**
- **Lombok**
- **Jakarta Validation**

---

## Authentication

The API uses **Spring Security** with role-based access control.

Roles available:

- `USER`
- `ADMIN`

Some endpoints are public, while others require authentication.

---

## Auth Endpoints

---

### Register

POST `/auth/register`

Request:

```json
{
  "username": "john",
  "password": "password123"
}
```

Response:

- 201 CREATED - User registered

- 409 CONFLICT - Username already exists

---

### Login

POST /auth/login

Request:

```json
{
  "username": "john",
  "password": "password123"
}
```

Response:

- 200 OK – Login successful

- 401 Unauthorized – Invalid credentials

---

## Blog Post Endpoints

---

### Create a Blog Post

POST /posts

Requires authentication.

Request:

```json
{
  "title": "My First Blog Post",
  "content": "This is the content of my first blog post.",
  "category": "Technology",
  "tags": ["Tech", "Programming"]
}
```

Response:

- 201 Created

- 400 Bad Request

---

### Update a Blog Post

PUT /posts/{id}

Only the author or an admin can update the post.

Response:

- 200 OK

- 403 Forbidden

- 404 Not Found

---

### Delete a Blog Post

DELETE /posts/{id}

Only the author or an admin can delete the post.

Response:

- 204 No Content

- 403 Forbidden

- 404 Not Found

---

### Get a Single Blog Post

GET /posts/{id}

Public endpoint.

Response:

- 200 OK

- 404 Not Found

---

### Get All Blog Posts

GET /posts

Public endpoint.

Response:

- 200 OK

Example response:

```json
[
  {
    "id": 1,
    "title": "My First Blog Post",
    "content": "This is the content of my first blog post.",
    "category": "Technology",
    "tags": ["Tech", "Programming"],
    "createdAt": "2021-09-01T12:00:00Z",
    "updatedAt": "2021-09-01T12:00:00Z"
  }
]
```

---

### Filter Blog Posts

You can filter posts using a search term.

GET /posts?term=tech

This performs a wildcard search on:

- title
- content
- category

---

#### Testing

Unit tests are implemented using JUnit and Mockito.

Run tests with:

```Linux / macOS
./mvnw test
```

```Windows
mvnw.cmd test
```

---

#### Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/lautarorisso/blogging_platform_api/
│   │       ├── BloggingPlatformApiApplication.java
│   │       ├── config/
│   │       │   ├── SecurityConfig.java
│   │       │   └── SwaggerConfig.java
│   │       ├── controller/
│   │       │   ├── AuthController.java
│   │       │   └── PostController.java
│   │       ├── dto/
│   │       ├── exception/
│   │       ├── mapper/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── security/
│   │       └── service/
│   └── resources/
│       ├── application.properties
│       └── static/
└── test/
    ├── java/
    │   └── com/lautarorisso/blogging_platform_api/
    │       └── service/
    └── resources/
        └── application-test.properties
```

---
