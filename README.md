# Radix Arithmetic Integration Testing Service

## Overview

Radix Arithmetic Integration Testing Service is a Spring Boot–based REST application designed to demonstrate production-grade database migration management and containerized integration testing.  

The system performs arithmetic operations on numbers represented in different numeral systems and persists every computation in a PostgreSQL database.  

The primary focus of this project is not only arithmetic correctness, but also architectural integrity, reproducibility, and realistic integration testing using Flyway and Testcontainers.

This project showcases modern backend engineering practices aligned with enterprise standards.

---

## Project Objectives

The application was designed to demonstrate:

• Controlled schema management using Flyway  
• Deterministic integration testing with Testcontainers  
• Containerized PostgreSQL environments for real database validation  
• Dynamic filtering using Spring Data JPA Specifications  
• Structured validation and error handling  
• JSON-based test data seeding using ObjectMapper  
• Strict separation of concerns between API, business logic, and persistence  

---

## Functional Description

The service supports arithmetic operations between numbers expressed in:

• Binary  
• Octal  
• Decimal  
• Hexadecimal  

Supported operations:

• Addition  
• Subtraction  
• Multiplication  
• Division  

All arithmetic is executed using BigInteger to ensure mathematical correctness without overflow.

Each request is persisted in the database along with:

• First operand (stored as string)  
• First operand radix  
• Second operand (stored as string)  
• Second operand radix  
• Operation type  
• Result value  
• Result radix  
• Timestamp of execution  

Numeric values are intentionally stored as strings to preserve exact input representation across numeral systems.

---

## REST API Capabilities

The system exposes two primary endpoints:

1. Calculation Endpoint  
   Performs arithmetic computation and persists the full request metadata.

2. Historical Query Endpoint  
   Retrieves stored computations within a specified time interval, with optional filtering by:
   • Operation type  
   • Left operand radix  
   • Right operand radix  

Filtering is implemented dynamically using JPA Specifications, ensuring flexible query composition.

---

## Database Architecture

Schema evolution is fully controlled by Flyway migrations.

Hibernate is configured in validation mode, meaning:

• Hibernate never creates or modifies schema  
• Schema must match migration definitions  
• Any mismatch results in startup failure  

This guarantees migration-driven schema governance and production-like safety.

Indexes are defined to optimize:

• Time-based queries  
• Operation-based filtering  
• Radix-based filtering  

---

## Integration Testing Strategy

Integration testing is implemented using:

• SpringBootTest  
• Testcontainers (PostgreSQL container)  
• DynamicPropertySource for runtime container configuration  
• ObjectMapper for JSON-based data seeding  

Testing characteristics:

• A real PostgreSQL container is started for test execution  
• Flyway migrations are automatically applied to the test container  
• Test entities are loaded from predefined JSON fixtures  
• Entities are persisted using JPA repositories  
• Expected responses are loaded from JSON and compared against actual API results  

This approach ensures:

• No reliance on in-memory databases  
• Realistic production-like database behavior  
• Fully isolated test execution  
• Deterministic and reproducible results  

---

## Validation and Error Handling

The application includes:

• Bean validation for request payloads  
• Centralized exception handling  
• Structured error responses  
• Correct HTTP status mapping  

Invalid input results in client errors.  
Arithmetic violations such as division by zero are handled explicitly.

---

## Architectural Structure

The project follows a layered architecture:

API Layer  
Handles REST communication, validation, and DTO mapping.

Core Layer  
Contains arithmetic logic and numeral system handling.

Persistence Layer  
Manages entity mapping and repository interaction.

Migration Layer  
Defines database schema through versioned Flyway scripts.

Test Layer  
Implements container-based integration verification.

This separation ensures clarity, maintainability, and scalability.

---

## Technology Stack

• Java 17  
• Spring Boot 3  
• Spring Data JPA  
• PostgreSQL  
• Flyway  
• Testcontainers  
• Docker  
• Maven  
• Jackson ObjectMapper  

---

## Execution Workflow

1. PostgreSQL is started (Docker or containerized environment).  
2. Application starts and Flyway validates or applies migrations.  
3. Hibernate validates schema consistency.  
4. REST endpoints become available.  
5. Integration tests can be executed, automatically spinning up isolated database containers.  

---

## Academic and Technical Significance

This project demonstrates:

• Migration-first database management  
• Production-grade schema validation practices  
• Real database integration testing with container orchestration  
• JSON-driven deterministic test design  
• Clean separation of responsibilities in service architecture  

It reflects a backend implementation aligned with modern engineering standards and emphasizes reliability, reproducibility, and correctness.

---

## Conclusion

Radix Arithmetic Integration Testing Service is not merely a calculator application.  
It is a demonstration of controlled database evolution, containerized integration testing, and disciplined backend architecture.

The project highlights how correctness, persistence integrity, and reproducible testing can be combined into a cohesive and production-ready backend service.
