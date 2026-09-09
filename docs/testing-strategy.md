# Testing Strategy

## Overview

The project uses multiple testing levels to validate application behavior.


Unit Tests
     +
Integration Tests
     +
Security Tests
     +
Performance Regression Tests


## Unit Testing

Unit tests focus on individual service-layer behavior.

The repository dependencies are mocked using Mockito.

Covered scenarios include:

* Successful URL creation
* URL not found
* Inactive URL
* Expired URL
* Valid URL lookup
* Analytics retrieval
* Invalid protocol rejection

## Integration Testing

Integration tests validate the complete application flow.


HTTP Request
      |
      v
Controller
      |
      v
Service
      |
      v
Repository
      |
      v
H2 Database


Covered scenarios include:

* Create URL
* Invalid URL request
* Missing short code
* Analytics endpoint
* Expired URL handling

## Security Testing

Security-focused tests verify that unsupported URL schemes are rejected.

Examples:


ftp://example.com
file:///example
javascript:example


Valid examples:


https://example.com
http://example.com


## Performance Regression Testing

The project includes a basic performance regression test.

The test creates multiple URLs and verifies that the operation completes within an acceptable threshold.

This is not intended to replace a full production load-testing tool.

Future load testing could use:

* JMeter
* Gatling
* k6

## Running Tests

### Windows

powershell
.\mvnw.cmd clean verify


### Linux/macOS


./mvnw clean verify


Expected result:


BUILD SUCCESS


## Continuous Integration

GitHub Actions automatically runs:


Checkout Code
      |
      v
Setup Java 21
      |
      v
Build Application
      |
      v
Run Tests
      |
      v
Verify Build

