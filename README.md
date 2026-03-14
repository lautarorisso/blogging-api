# Blogging Platform API

A simple RESTful API for a personal blogging platform built with **Java, Spring Boot, and PostgreSQL**.  
The API allows users to create and manage blog posts while implementing authentication and role-based authorization using **Spring Security**.

---

# Features

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

- **Java**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger / OpenAPI**
- **JUnit / Mockito**

---

# Authentication

The API uses **Spring Security** with role-based access control.

Roles available:

- `USER`
- `ADMIN`

Some endpoints are public, while others require authentication.

---

# Auth Endpoints

## Register

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

## Login

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

## Blog Post Endpoints

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

### Update a Blog Post

PUT /posts/{id}

Only the author or an admin can update the post.

Response:

- 200 OK

- 403 Forbidden

- 404 Not Found

### Delete a Blog Post

DELETE /posts/{id}

Only the author or an admin can delete the post.

Response:

- 204 No Content

- 403 Forbidden

- 404 Not Found

### Get a Single Blog Post

GET /posts/{id}

Public endpoint.

Response:

- 200 OK

- 404 Not Found

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

#### Running the Project

1. Clone the repository

   git clone https://github.com/your-username/blogging-api.git

2. Configure the database

Update application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/blog_db
spring.datasource.username=postgres
spring.datasource.password=your_password

3. Run the application

```Linux / macOS
./mvnw spring-boot:run
```

```Windows
mvnw.cmd spring-boot:run
```

or run the main class from your IDE.

#### API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui.html

or

http://localhost:8080/swagger-ui/index.html

#### Testing

Unit tests are implemented using JUnit and Mockito.

Run tests with:

```Linux / macOS
./mvnw test
```

```Windows
mvnw.cmd test
```

#### Project Structure

src
-└ main
--└ java
--├ config
--├ controller
--├ dto
--├ mapper
--├ model
--├ repository
--├ security
--└ service

#### roadmap.sh

https://roadmap.sh/projects/blogging-platform-api
