# BDD Test Suite for Calculator Service (Cucumber + Gherkin)

## Overview
This project implements Behavior-Driven Development (BDD) testing for a Spring Boot calculator service. The BDD layer is built with Gherkin scenarios and executed via Cucumber on the JUnit Platform. The tests validate the service from the consumer perspective by driving the public REST API and verifying both HTTP responses and persisted calculation data.

The calculator service supports mixed-radix operands (e.g., BIN and HEX) and stores each calculation in a PostgreSQL database. The BDD suite covers successful business flows, search/filter behavior, and error handling.

## Goals
- Introduce Gherkin as a formal specification language for user behavior.
- Implement executable specifications using Cucumber.
- Verify end-to-end behavior of the calculator service via its REST API.
- Ensure database determinism and scenario isolation through controlled baseline state.

## User Stories Captured in Gherkin
The feature specification models user-centric behavior:
- As a user, I can calculate using operands in different radixes and receive a result in the requested radix.
- As a user, I can search stored calculations by time range and operation type.
- As a user, I can provide operand collections in different forms (table, class-like mapping, custom delimiter format).
- As a user, I receive a validation error when a request is invalid (e.g., division by zero).

## Test Strategy
### Execution Model
- Cucumber runs on the JUnit 5 Platform using the Cucumber test engine.
- A Spring Boot test context is started with a random web port to avoid port conflicts.
- Test steps perform real HTTP calls to the application endpoints and assert:
  - HTTP status codes
  - JSON response structure and values
  - Existence of persisted metadata (e.g., identifiers and timestamps)
  - Search result shape and selected fields

### Scenario Isolation and Database Control
To ensure repeatability and independence of scenarios:
- A baseline database state is established from a JSON fixture.
- The baseline is applied before scenarios through the feature-level Background step.
- After each scenario, database state is restored to the same baseline using Cucumber hooks.
- Restoration uses a deterministic reset strategy (truncate + identity reset + fixture reinsert), preventing test coupling and flakiness.

## Collection Input Coverage
The BDD suite includes mandatory coverage for multiple ways of passing collections:
- DataTable input (tabular representation in Gherkin).
- Class-like table mapping (row-based mapping into a domain-shaped structure).
- Custom delimiter format (single argument string parsed into structured operands).
- Multi-argument scenario (explicit separate operands and operation parameters).

## Project Structure (BDD Layer)
Typical locations used by the BDD suite:
- Feature specifications in test resources.
- Cucumber configuration in test resources for discovery (features, glue, output plugins).
- Fixture datasets in test resources.
- Step definitions, hooks, and custom parameter types under the BDD package in test sources.
- JUnit Platform entrypoint for running the Cucumber engine.

## How to Run
Run the Cucumber BDD suite with Maven by targeting the Cucumber entrypoint test class:

- `mvn -Dtest=RunCucumberTest clean test`

Expected outcome:
- All scenarios are executed successfully.
- The build finishes with a success status.
- Cucumber outputs a scenario/step summary to the console.

## Key Result
The laboratory requirements are satisfied by:
- User stories expressed in Gherkin.
- Executable BDD scenarios implemented with Cucumber.
- Database baseline applied via Background and restored via hooks.
- Presence of scenarios for DataTable, class-like mapping, custom delimiter parsing, and multi-argument validation behavior.

## Technologies
- Java 17
- Spring Boot (Web, Validation, Data JPA)
- PostgreSQL
- Flyway (schema migrations)
- Cucumber (cucumber-java, cucumber-spring, cucumber-junit-platform-engine)
- JUnit Platform (JUnit 5)
- Maven (Surefire)
- Jackson (JSON parsing for fixtures and response validation)
