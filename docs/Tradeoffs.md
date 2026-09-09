# Engineering Trade-offs

## H2 vs Production Database

### Decision

Use H2 for development.

### Benefits

* Easy setup
* No external database installation
* Fast local development
* Simple automated testing

### Trade-off

H2 is not the preferred choice for a high-scale production environment.

A future production implementation could use PostgreSQL.



## No Redis

### Decision

Redis was intentionally removed.

### Benefits

* Reduced infrastructure complexity
* Fewer services to manage
* Easier local setup
* Lower operational overhead

### Trade-off

High-volume redirect workloads could benefit from caching.

Redis can be introduced later if performance requirements justify it.



## Random Short Codes

### Decision

Generate short codes using SecureRandom.

### Benefits

* Simple implementation
* Large possible code space
* Better unpredictability

### Trade-off

A collision check requires a database query.

Alternative approaches include:

* Base62 encoded database IDs
* Distributed ID generators



## Click Event Storage

### Decision

Store click events for analytics.

### Benefits

* Simple analytics implementation
* Historical click tracking

### Trade-off

A high-volume production system could generate a large number of records.

Future architectures could process analytics asynchronously using Kafka or a message queue.



## Synchronous Processing

### Decision

Keep the initial application workflow synchronous.

### Benefits

* Simple architecture
* Easy debugging
* Easy testing

### Trade-off

High-volume analytics processing may eventually require asynchronous processing.



## Spring Boot Layered Architecture

### Decision

Use Controller, Service, Repository layers.

### Benefits

* Separation of concerns
* Easier testing
* Easier maintenance

### Trade-off

Small applications may have additional boilerplate.

The structure is retained because it demonstrates scalable backend design practices.
