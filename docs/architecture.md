# Architecture

## Overview

The URL Shortener is built using a layered Spring Boot architecture.

The main goal is to separate HTTP handling, business logic, and database operations.


                    Client
                       |
                       v
                REST Controller
                       |
                       v
                 Service Layer
                       |
                       v
                Repository Layer
                       |
                       v
                   H2 Database


## Controller Layer

The controller layer is responsible for:

* Receiving HTTP requests
* Validating request data
* Returning HTTP responses
* Delegating business logic to services

Typical endpoints include:


POST /api/urls
GET /{shortCode}
GET /api/urls/{shortCode}/analytics


## Service Layer

The service layer contains the application's business logic.

Responsibilities include:

* URL validation
* Short-code generation
* Collision handling
* URL expiration validation
* Active/inactive URL validation
* Analytics processing

The service layer prevents business logic from being placed directly inside controllers.

## Repository Layer

Spring Data JPA repositories handle database access.

Responsibilities include:

* Finding URLs by short code
* Checking short-code uniqueness
* Saving URLs
* Counting click events

Example optimized operations:


Find URL by short code
Count click events by URL ID


## Database Layer

The application uses H2 for development.

Main entities:


Url
ClickEvent


### Url

Stores:

* Original URL
* Short code
* Created timestamp
* Expiration timestamp
* Active status

### ClickEvent

Stores:

* URL reference
* Click timestamp

## Request Flow

### Creating a Short URL


POST /api/urls
        |
        v
UrlController
        |
        v
UrlService
        |
        ├── Validate URL
        |
        ├── Generate Short Code
        |
        ├── Check Uniqueness
        |
        v
UrlRepository
        |
        v
H2 Database


### Redirect Flow


GET /{shortCode}
        |
        v
UrlController
        |
        v
UrlService
        |
        ├── Find URL
        ├── Check Active Status
        ├── Check Expiration
        |
        v
Return Redirect


### Analytics Flow

text
URL Click
    |
    v
ClickEvent
    |
    v
ClickEventRepository
    |
    v
H2 Database
    |
    v
COUNT Query
    |
    v
Analytics Response


## Performance Design

The most frequently used database operations are optimized through indexes.


short_code
    |
    v
Indexed Lookup


Click analytics uses:


COUNT(*)


instead of loading all click events into memory.

## Security Design

The application validates URLs before persistence.

Allowed protocols:

text
HTTP
HTTPS


Unsupported protocols are rejected.

The application also uses:

* Centralized exception handling
* Restricted CORS configuration
* Security headers
* Input validation

## CI Architecture


Developer Push
       |
       v
GitHub Repository
       |
       v
GitHub Actions
       |
       ├── Java 21
       ├── Maven Cache
       ├── Build
       └── Tests

