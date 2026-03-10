Feature: Angular calculator end-to-end testing

  Background:
    Given I open the calculator page

  Scenario: Page contains inputs, dropdowns and button
    Then left input should exist
    And right input should exist
    And operation dropdown should exist
    And all dropdowns should exist
    And calculate button should exist

  Scenario Outline: Arithmetic operations work correctly
    Given I choose "<leftRadix>" for the left operand
    And I choose "<rightRadix>" for the right operand
    And I choose "<resultRadix>" for the result
    And I choose operation "<operation>"
    And I enter "<leftValue>" into the left input
    And I enter "<rightValue>" into the right input
    When I click calculate
    Then result text should contain "<expectedResult>"
    And decimal result text should contain "<expectedDec>"

    Examples:
      | leftValue | leftRadix | operation | rightValue | rightRadix | resultRadix | expectedResult | expectedDec |
      | 10        | DEC       | +         | 5          | DEC        | DEC         | 15             | 15.00       |
      | 10        | DEC       | -         | 5          | DEC        | DEC         | 5              | 5.00        |
      | 10        | DEC       | *         | 5          | DEC        | DEC         | 50             | 50.00       |
      | 10        | DEC       | /         | 5          | DEC        | DEC         | 2              | 2.00        |

  Scenario: Only valid decimal digits are allowed in decimal mode
    Given I choose "DEC" for the left operand
    When I enter "12AB34" into the left input
    Then left input value should be "1234"

  Scenario: Zero cannot be entered into the second input during division
    Given I choose operation "/"
    When I enter "0" into the right input
    Then right input value should be ""

  Scenario: Hexadecimal letters are allowed in HEX mode
    Given I choose "HEX" for the left operand
    When I enter "1A2F" into the left input
    Then left input value should be "1A2F"

  Scenario Outline: Result color depends on sign
    Given I choose "DEC" for the left operand
    And I choose "DEC" for the right operand
    And I choose "DEC" for the result
    And I choose operation "<operation>"
    And I enter "<leftValue>" into the left input
    And I enter "<rightValue>" into the right input
    When I click calculate
    Then decimal result color should be "<color>"

    Examples:
      | leftValue | operation | rightValue | color |
      | 10        | -         | 20         | red   |
      | 10        | -         | 10         | black |
      | 20        | -         | 10         | green |
