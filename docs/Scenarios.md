# Engineering Scenarios

## Scenario 1: Greenfield Development

### Situation

A new URL Shortener application needed to be built from scratch.

### Challenge

The application needed to provide:

* URL creation
* Unique short codes
* Redirect functionality
* Analytics
* URL expiration
* Error handling
* Automated testing

### Approach

A Spring Boot layered architecture was selected.


Controller
    |
Service
    |
Repository
    |
Database


The application was implemented incrementally:

1. Create project structure
2. Configure H2 database
3. Create entities
4. Create repositories
5. Implement services
6. Create REST APIs
7. Add expiration support
8. Add analytics
9. Add testing
10. Add CI/CD

### Result

The project provides a complete backend prototype with automated validation.



## Scenario 2: Brownfield Improvement

### Situation

An existing lightweight implementation needed improvement.

### Problems

Potential issues included:

* Missing indexes
* Inefficient analytics queries
* Uncontrolled short-code generation
* Missing security validation
* Limited automated testing

### Improvements

The application was improved by adding:

* Database indexes
* Efficient COUNT queries
* SecureRandom short-code generation
* Collision retry limits
* URL protocol validation
* Unit tests
* Integration tests
* GitHub Actions CI

### Result

The application became easier to maintain, test, and validate.



## Scenario 3: Ambiguous Requirement

### Requirement

The system should support URL expiration.

### Ambiguity

Several questions needed architectural decisions:

* Is expiration optional?
* What happens after expiration?
* Should expired URLs be deleted?
* Which HTTP response should be returned?

### Decision

The design uses:

* Optional expiration
* URL remains stored after expiration
* Redirect access is blocked after expiration
* HTTP 410 Gone is returned

### Reasoning

Keeping the record provides historical information while preventing users from accessing expired links.

### Result

The requirement was converted into explicit and testable behavior.
