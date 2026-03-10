# Frontend Testing Overview

The project includes both client-side unit tests in Angular and end-to-end (E2E) tests with Cypress to ensure robust frontend functionality.

## Unit Testing (Angular)

Frontend unit tests cover:

- UI component behavior in isolation
- Presentation logic and reusable building blocks
- Custom directives and pipes
- Input validation
- Result rendering, including:
  - input filtering by numeral system
  - prevention of invalid division input
  - result coloring based on sign
  - formatting via custom pipe

These tests guarantee that client-side logic is independently testable and reliable.

## End-to-End Testing (Cypress)

E2E tests simulate full user interactions:

- Cypress-based browser automation using Page Object pattern
- Gherkin-style scenario descriptions
- Verification of UI elements:
  - inputs, dropdowns, action buttons
  - correctness of arithmetic operations
  - input restrictions (no invalid characters, no zero in divisor)
  - support for HEX mode input
  - correct result coloring based on sign
- Cypress execution modes:
  - headless for automated CI runs
  - interactive for manual testing

This testing layer validates complete user-facing workflows and ensures the interface behaves as expected.
