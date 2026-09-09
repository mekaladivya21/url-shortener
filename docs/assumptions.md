# Assumptions

## Application Scope

This application is designed as a portfolio and learning project demonstrating backend engineering concepts.

## Database

H2 is used as the development database.

The design allows migration to a production database such as PostgreSQL or MySQL.

## URL Protocols

Only HTTP and HTTPS URLs are supported.

Other protocols are intentionally rejected.

## Short Codes

Short codes are generated using SecureRandom.

The application checks for collisions before saving a generated short code.

A maximum retry limit is used to prevent unlimited generation attempts.

## URL Expiration

An expiration date is optional.

When an expiration date exists and the current time passes that date, the short URL is considered expired.

## Analytics

Each redirect can generate a click event.

Analytics are calculated using database count queries.

## Authentication

Authentication and authorization are outside the current project scope.

## Deployment

The project currently focuses on local execution and CI validation.

Production deployment infrastructure is considered a future enhancement.
