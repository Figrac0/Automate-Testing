import { Before, Given, When, Then } from '@badeball/cypress-cucumber-preprocessor';
import { CalculatorPage } from '../../support/pages/calculator.page';

const page = new CalculatorPage();

Before(() => {
  page.mockCalculatorApi();
});

Given('I open the calculator page', () => {
  page.visit();
});

Then('left input should exist', () => {
  page.leftInput().should('exist');
});

Then('right input should exist', () => {
  page.rightInput().should('exist');
});

Then('operation dropdown should exist', () => {
  page.operationSelect().should('exist');
});

Then('all dropdowns should exist', () => {
  page.allDropdowns().should('have.length', 4);
});

Then('calculate button should exist', () => {
  page.calculateButton().should('exist');
});

Given('I choose {string} for the left operand', (radix: string) => {
  page.selectLeftRadix(radix);
});

Given('I choose {string} for the right operand', (radix: string) => {
  page.selectRightRadix(radix);
});

Given('I choose {string} for the result', (radix: string) => {
  page.selectResultRadix(radix);
});

Given('I choose operation {string}', (operation: string) => {
  page.selectOperation(operation);
});

When('I enter {string} into the left input', (value: string) => {
  page.setLeftValue(value);
});

When('I enter {string} into the right input', (value: string) => {
  page.setRightValue(value);
});

When('I click calculate', () => {
  page.clickCalculate();
  page.waitForCalculation();
});

Then('result text should contain {string}', (expected: string) => {
  page.resultRadixText().should('contain.text', expected);
});

Then('decimal result text should contain {string}', (expected: string) => {
  page.resultDecText().should('contain.text', expected);
});

Then('left input value should be {string}', (expected: string) => {
  page.leftInput().should('have.value', expected);
});

Then('right input value should be {string}', (expected: string) => {
  page.rightInput().should('have.value', expected);
});

Then('decimal result color should be {string}', (color: string) => {
  page
    .resultDecText()
    .should('have.css', 'color')
    .then((actualColor) => {
      const allowed: Record<string, string[]> = {
        red: ['rgb(255, 0, 0)', 'red'],
        black: ['rgb(0, 0, 0)', 'black'],
        green: ['rgb(0, 128, 0)', 'green'],
      };

      expect(allowed[color]).to.include(actualColor);
    });
});
