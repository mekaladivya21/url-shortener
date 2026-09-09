# URL Shortener

A production-style URL Shortener REST API built using Java 21 and Spring Boot 4.x.

The application allows users to create short URLs, redirect users to the original URL, track click analytics, manage URL expiration, and handle invalid requests through centralized exception handling.

## Features

* Create short URLs
* Redirect short URLs to original URLs
* Generate unique short codes
* Track URL click events
* Retrieve URL analytics
* Support URL expiration
* Validate HTTP and HTTPS URLs
* Reject unsupported URL protocols
* Global exception handling
* H2 database integration
* Unit testing
* Integration testing
* Performance-focused testing
* GitHub Actions CI pipeline

## Technology Stack

| Technology      | Purpose                |
| --------------- | ---------------------- |
| Java 21         | Programming language   |
| Spring Boot 4.x | Application framework  |
| Spring MVC      | REST API layer         |
| Spring Data JPA | Database access        |
| Hibernate       | ORM                    |
| H2 Database     | Development database   |
| Maven           | Build management       |
| JUnit 5         | Testing                |
| Mockito         | Unit testing           |
| GitHub Actions  | Continuous Integration |

## Architecture

The application follows a layered architecture:

```text
Client
   |
   v
Controller Layer
   |
   v
Service Layer
   |
   v
Repository Layer
   |
   v
H2 Database
```

## Project Structure

```text
src/main/java/com/divya/urlshortener
│
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
└── UrlShortenerApplication.java
```

## Requirements

Install:

* Java 21
* Git

The project uses the Maven Wrapper, so a separate Maven installation is optional.

Verify Java:

```bash
java -version
```

Verify:

```text
Java 21
```

## Running the Application

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux or macOS

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

## H2 Database

The application uses H2 for development.

The H2 console can be enabled through application configuration.

Typical console URL:

```text
http://localhost:8080/h2-console
```

Use the JDBC URL configured in:

```text
src/main/resources/application.properties
```

## API Endpoints

### Create Short URL

```http
POST /api/urls
```

Example request:

```json
{
  "originalUrl": "https://www.example.com"
}
```

Expected response:

```json
{
  "originalUrl": "https://www.example.com",
  "shortCode": "abc12345",
  "shortUrl": "http://localhost:8080/abc12345"
}
```

### Redirect to Original URL

```http
GET /{shortCode}
```

The application redirects the user to the original URL.

### Get Analytics

```http
GET /api/urls/{shortCode}/analytics
```

Example response:

```json
{
  "shortCode": "abc12345",
  "totalClicks": 10
}
```

## URL Validation

The application accepts:

```text
http://
https://
```

The application rejects unsupported protocols such as:

```text
ftp://
file://
javascript:
```

## Testing

Run all tests:

### Windows

```powershell
.\mvnw.cmd clean verify
```

### Linux/macOS

```bash
./mvnw clean verify
```

The project includes:

* Unit tests
* Integration tests
* Security validation tests
* Basic performance regression tests

## CI/CD

GitHub Actions automatically runs the build and tests when code is pushed or a Pull Request is created.

Pipeline:

```text
Push / Pull Request
        |
        v
GitHub Actions
        |
        v
Java 21 Setup
        |
        v
Maven Build
        |
        v
Run Tests
        |
        v
Build Verification
```

## Performance Considerations

The application includes:

* Database indexes for short-code lookups
* Indexed click-event queries
* Database COUNT queries for analytics
* SecureRandom for short-code generation
* Collision retry limits
* Transaction optimization where appropriate

Redis was intentionally not included to keep the project lightweight and optimized around H2 and database access.

## Future Improvements

Possible future enhancements include:

* PostgreSQL or MySQL for production
* Redis caching for high-volume redirects
* Docker support
* Kubernetes deployment
* Rate limiting
* Authentication and authorization
* Custom domains
* QR code generation
* Advanced analytics
* Geographic click tracking

## Build Status

The project uses GitHub Actions for continuous integration.

The CI pipeline validates that the application builds successfully with Java 21 and executes the test suite automatically.

## License

This project is intended for learning, demonstration, and portfolio purposes.
