# Blogging Platform API

A simple RESTful API for a personal blogging platform built with **Java, Spring Boot, and PostgreSQL**.  
The API provides basic CRUD operations for managing blog posts and follows REST best practices.

## Features

- Create a blog post
- Update an existing blog post
- Delete a blog post
- Retrieve a single blog post
- Retrieve all blog posts
- Filter blog posts by a search term
- API documentation with Swagger
- Unit testing

## Tech Stack

- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger / OpenAPI**
- **JUnit / Mockito**

## API Endpoints

### Create a Blog Post

POST /posts

Request body:

```json
{
  "title": "My First Blog Post",
  "content": "This is the content of my first blog post.",
  "category": "Technology",
  "tags": ["Tech", "Programming"]
}
```

Response:

- 201 Created – Post created successfully

- 400 Bad Request – Validation error

Example response:

```json
{
  "id": 1,
  "title": "My First Blog Post",
  "content": "This is the content of my first blog post.",
  "category": "Technology",
  "tags": ["Tech", "Programming"],
  "createdAt": "2021-09-01T12:00:00Z",
  "updatedAt": "2021-09-01T12:00:00Z"
}
```

### Update a Blog Post

PUT /posts/{id}

Example:

PUT /posts/1

Request body:

```json
{
  "title": "My Updated Blog Post",
  "content": "This is the updated content of my first blog post.",
  "category": "Technology",
  "tags": ["Tech", "Programming"]
}
```

Response:

- 200 OK – Post updated successfully

- 400 Bad Request – Validation error

- 404 Not Found – Post not found

### Delete a Blog Post

DELETE /posts/{id}

Example:

DELETE /posts/1

Response:

- 204 No Content – Successfully deleted

- 404 Not Found – Post not found

### Get a Single Blog Post

GET /posts/{id}

Example:

GET /posts/1

Response:

- 200 OK – Returns the blog post

- 404 Not Found – Post not found

### Get All Blog Posts

GET /posts

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

### Filter Blog Posts

You can filter posts using a search term.

GET /posts?term=tech

This performs a wildcard search on:

title

content

category

#### Running the Project

1. Clone the repository
   git clone https://github.com/your-username/blogging-api.git
2. Configure the database

Update application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
spring.datasource.username=your_username
spring.datasource.password=your_password 3. Run the application
./mvnw spring-boot:run

or run the main class from your IDE.

#### API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui.html

or

http://localhost:8080/swagger-ui/index.html

#### Testing

Unit tests are implemented using JUnit and Mockito.

Run tests with:

./mvnw test

#### Project Structure

src
└─ main
  └─ java
    ├─ controller
    ├─ service
    ├─ repository
    ├─ model
    └─ dto

#### roadmap.sh

https://roadmap.sh/projects/blogging-platform-api
