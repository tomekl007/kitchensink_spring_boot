# Kitchensink Spring Boot Application

A Spring Boot web application that demonstrates basic CRUD operations with JPA, REST endpoints, and a simple web interface.

## Features

- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- REST API
- Thymeleaf templates
- Validation
- Unit and Integration Tests

## Requirements

- Java 21
- Maven 3.6+

## Building the Application

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will be available at http://localhost:8080

## API Endpoints

- GET /rest/members - List all members
- GET /rest/members/{id} - Get a specific member
- POST /rest/members - Create a new member
- PUT /rest/members/{id} - Update a member
- DELETE /rest/members/{id} - Delete a member

## Testing

Run the tests with:

```bash
mvn test
```

## License

This project is licensed under the Apache License, Version 2.0. 