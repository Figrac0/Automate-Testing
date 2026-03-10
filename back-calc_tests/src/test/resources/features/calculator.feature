Feature: Calculator service stores calculations and supports mixed radixes

  Background:
    Given database seeded from "fixtures/input_calculations.json"

  Scenario: User calculates sum of mixed-radix operands and gets result in requested radix
    When I calculate left "101" in "BIN" and right "A" in "HEX" with op "+" expecting radix "DEC"
    Then response status is 200
    And calc response has result "15" and radix "DEC"

  Scenario: User searches calculations by time range and operation
    When I search calculations from "2026-02-26T23:59:59Z" to "2026-02-27T00:00:01Z" with operation "ADD"
    Then response status is 200
    And search response size is 1
    And first item has operation "ADD" leftRadix "BIN" rightRadix "HEX"

  Scenario: Collections via DataTable - calculate using operands passed as a table
    When I calculate using table operands with op "+" expecting radix "DEC"
      | side  | value | radix |
      | left  | 101   | BIN   |
      | right | A     | HEX   |
    Then response status is 200
    And calc response has result "15" and radix "DEC"

  Scenario: Table mapped to class - calculate using operands passed in class-like table rows
    When I calculate using class table with op "+" expecting radix "DEC"
      | 101 | BIN | left  |
      | A   | HEX | right |
    Then response status is 200
    And calc response has result "15" and radix "DEC"

  Scenario: Custom delimiter - calculate using operands in one argument
    When I calculate using class operands "101|BIN;A|HEX" with op "+" expecting radix "DEC"
    Then response status is 200
    And calc response has result "15" and radix "DEC"

  Scenario: Multiple arguments - division by zero returns 422
    When I calculate left "10" in "DEC" and right "0" in "DEC" with op "/" expecting radix "DEC"
    Then response status is 422