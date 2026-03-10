# Multi-Level Testing of a Full-Stack Calculator System

A practice-oriented academic project focused on systematic software testing across multiple layers of a full-stack application.  
The repository combines backend and frontend development with an emphasis on verification quality, test design, automation, and reproducibility.

The core application is a calculator that performs arithmetic operations across multiple numeral systems. However, the main value of the project is not the calculator itself, but the complete testing pipeline built around it: unit testing, integration testing, BDD testing, API testing, frontend unit testing, and end-to-end testing.

## 📸 Tests Preview

<div align="center">

| 1 | 2 |
| :---: | :---: |
| <img src="https://github.com/Figrac0/Automate-Testing/blob/MVP-tests/client/cypress/99.png" width="400"/> | <img src="https://github.com/Figrac0/Automate-Testing/blob/API_testing/images/2.png" width="400"/> |

| 3 | 4 |
| :---: | :---: |
| <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/4.png" width="400"/> | <img src="https://github.com/Figrac0/Automate-Testing/blob/API_testing/images/1.png" width="400"/> |

</div>

---


## Project Focus

This project demonstrates a structured approach to software quality assurance through the following testing domains:

- backend unit testing
- backend integration testing with containers and database migrations
- BDD testing with Cucumber and Gherkin
- API testing in Postman
- performance and load testing in JMeter
- frontend unit testing in Angular
- frontend end-to-end testing in Cypress with Page Object and Gherkin-style scenarios

## Technology Stack

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Testcontainers
- JUnit 5
- Cucumber

### Frontend
- Angular
- TypeScript
- Jasmine
- Karma
- Cypress

### API and Performance Testing
- Postman
- JMeter

## Functional Domain

The system performs arithmetic operations:
- addition
- subtraction
- multiplication
- division

Supported numeral systems:
- binary
- octal
- decimal
- hexadecimal

The backend exposes REST endpoints for:
- calculation execution
- persistence of calculation history
- filtered retrieval of performed calculations

The frontend provides a calculator interface connected to the backend service.

## Testing Architecture

The project was developed as a sequence of laboratory works, where each stage extended the previous one and introduced a new testing level.

### 1. Backend Unit Testing

The initial stage focused on unit testing of the calculator logic.

Main goals:
- validate arithmetic operations independently
- verify behavior across different numeral systems
- test exception handling
- organize tests in a clear and maintainable way

Covered techniques:
- parameterized tests
- CSV-based test input
- external test datasets
- dynamic tests
- nested test classes
- exception assertions

This stage established a solid foundation for validating the business logic before moving to more complex system-level testing.

### 2. Integration Testing

The next stage introduced integration testing for the Spring Boot backend.

Main goals:
- verify interaction between controller, service logic, persistence layer, and database
- validate database schema through migrations
- test the application against a real PostgreSQL instance
- reproduce realistic runtime conditions

Key practices:
- Flyway migrations for schema management
- PostgreSQL as the real target database
- Testcontainers for isolated integration test environments
- JSON fixtures for seeding and expected result comparison
- ObjectMapper for loading structured test data

This stage ensured that the backend works correctly not only in isolation, but also as a connected system.

### 3. BDD Testing with Cucumber

The project then extended integration tests into BDD scenarios.

Main goals:
- express functional requirements in business-readable form
- connect Gherkin scenarios to executable test steps
- validate end-to-end backend behavior from the user story perspective

Implemented features:
- Gherkin feature files
- step definitions in Java
- background setup for initial database state
- hooks for resetting database state after scenarios
- scenarios with:
  - tables
  - class-mapped objects
  - custom delimiters
  - multiple arguments

This stage transformed technical verification into behavior-oriented specifications.

### 4. API Testing in Postman

The backend API was further tested through Postman collections.

Main goals:
- validate HTTP requests and responses independently from the UI
- test request collections in a repeatable format
- verify environment-dependent execution
- support manual and automated API validation

Implemented practices:
- Postman environments
- collection-level variables
- reusable requests
- assertions inside test scripts
- collection export for reproducibility

This stage made API verification more accessible and practical for external testing and demonstration.

### 5. Performance and Load Testing in JMeter

The system was also evaluated from the performance perspective.

Main goals:
- measure API behavior under repeated or concurrent load
- identify whether the backend remains stable during multiple requests
- verify endpoint responsiveness outside unit and integration test scope

Testing focus:
- repeated requests to calculation endpoints
- response monitoring
- throughput and behavior observation under load
- practical introduction to non-functional testing

This stage complemented correctness testing with runtime behavior analysis.

### 6. Frontend Unit Testing in Angular

After backend validation, the project introduced a client-side Angular application and corresponding frontend unit tests.

Main goals:
- verify UI component behavior in isolation
- validate presentation logic
- test reusable frontend building blocks

Implemented frontend testing areas:
- component tests
- custom directive tests
- custom pipe tests
- input validation tests
- result rendering tests

Special frontend features covered by tests:
- input filtering by numeral system
- prevention of invalid division input
- result coloring based on sign
- formatting through a custom pipe

This stage ensured that the client-side logic is independently testable and robust.

### 7. End-to-End Testing with Cypress

The final stage introduced full frontend E2E testing.

Main goals:
- simulate real user interaction with the Angular application
- verify that the interface behaves correctly from the user perspective
- validate complete UI flows

Implemented features:
- Cypress-based browser automation
- Page Object pattern
- Gherkin-style test descriptions
- scenario-based validation of user behavior

Covered E2E checks:
- existence of inputs, dropdowns, and action button
- correctness of all arithmetic operations
- restriction of invalid input characters
- inability to enter zero into the second field during division
- support for hexadecimal characters in HEX mode
- correct visual color of the result depending on sign

Cypress execution modes:
- headless execution for automated runs
- interactive execution with:

```bash
npm run cypress:open
```

This stage completed the testing pipeline by validating the actual user-facing application behavior.

## Quality Assurance Strategy

The repository demonstrates a layered testing strategy:

- **unit tests** validate isolated logic
- **integration tests** validate system interaction
- **BDD tests** validate business behavior
- **API tests** validate endpoint contracts
- **load tests** validate runtime stability
- **frontend unit tests** validate UI logic
- **E2E tests** validate user workflows

This layered approach reduces blind spots and improves confidence in both correctness and maintainability.

## Key Educational Outcomes

This project demonstrates practical experience in:

- designing maintainable test suites
- combining white-box and black-box testing techniques
- structuring backend and frontend verification flows
- using containerized infrastructure for reproducible tests
- expressing requirements in executable BDD format
- testing REST APIs independently of the interface
- validating frontend logic at both unit and browser levels
- organizing multi-tool QA workflows in a single repository

## Repository Highlights

- backend unit testing with JUnit 5
- dynamic and parameterized test coverage
- containerized integration testing with Testcontainers
- database migration validation with Flyway
- BDD scenarios with Cucumber and Gherkin
- Postman API collections for request-level testing
- JMeter load testing for performance exploration
- Angular unit testing for components, directives, and pipes
- Cypress E2E browser tests with Page Object model
- a full-stack testing-oriented academic workflow

## Example Commands

### Backend tests
```bash
mvn test
```

### Angular unit tests
```bash
ng test
```

### Cypress E2E tests
```bash
npm run e2e
```

## Academic Positioning

This repository should be viewed as a **testing-centered educational project** rather than a calculator showcase.
The calculator serves as a compact functional domain that makes it possible to demonstrate multiple software testing methodologies on the same system, from isolated unit validation to full browser-driven end-to-end scenarios.

## Conclusion

The project provides a compact but comprehensive example of how one application can be validated through multiple complementary testing strategies.
Its primary contribution is a unified view of software quality assurance across backend, API, database, frontend, and end-user interaction layers.

The result is a reproducible, multi-level testing ecosystem suitable for academic demonstration, practical training, and further extension into more advanced QA workflows.

### Cypress interactive mode 
```bash
npm run cypress:open
```
