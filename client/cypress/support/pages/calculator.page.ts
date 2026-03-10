export class CalculatorPage {
  private readonly calcUrl = 'http://localhost:8080/api/calc';

  visit() {
    cy.visit('/');
  }

  leftInput() {
    return cy.get('[data-cy="left-input"]');
  }

  rightInput() {
    return cy.get('[data-cy="right-input"]');
  }

  leftRadix() {
    return cy.get('[data-cy="left-radix"]');
  }

  rightRadix() {
    return cy.get('[data-cy="right-radix"]');
  }

  resultRadix() {
    return cy.get('[data-cy="result-radix"]');
  }

  operationSelect() {
    return cy.get('[data-cy="operation-select"]');
  }

  allDropdowns() {
    return cy.get('[data-cy="left-radix"], [data-cy="operation-select"], [data-cy="right-radix"], [data-cy="result-radix"]');
  }

  calculateButton() {
    return cy.get('[data-cy="calculate-btn"]');
  }

  resultBox() {
    return cy.get('[data-cy="result-box"]');
  }

  resultRadixText() {
    return cy.get('[data-cy="result-radix-text"]');
  }

  resultDecText() {
    return cy.get('[data-cy="result-dec-text"]');
  }

  errorMessage() {
    return cy.get('[data-cy="error-message"]');
  }

  setLeftValue(value: string) {
    this.leftInput().clear().type(value);
  }

  setRightValue(value: string) {
    this.rightInput().clear().type(value);
  }

  selectLeftRadix(radix: string) {
    this.leftRadix().select(radix);
  }

  selectRightRadix(radix: string) {
    this.rightRadix().select(radix);
  }

  selectResultRadix(radix: string) {
    this.resultRadix().select(radix);
  }

  selectOperation(op: string) {
    this.operationSelect().select(op);
  }

  clickCalculate() {
    this.calculateButton().click();
  }

  waitForCalculation() {
    cy.wait('@calculate');
  }

  mockCalculatorApi() {
    cy.intercept('POST', this.calcUrl, (req) => {
      const { left, right, op, resultRadix } = req.body as {
        left: { value: string; radix: string };
        right: { value: string; radix: string };
        op: '+' | '-' | '*' | '/';
        resultRadix: 'BIN' | 'OCT' | 'DEC' | 'HEX';
      };

      const leftValue = this.parseValue(left.value, left.radix);
      const rightValue = this.parseValue(right.value, right.radix);
      const result = this.calculate(leftValue, rightValue, op);

      req.reply({
        statusCode: 200,
        body: {
          id: 1,
          result: this.formatValue(result, resultRadix),
          resultRadix,
          createdAt: '2026-03-10T12:00:00Z',
        },
      });
    }).as('calculate');
  }

  private parseValue(value: string, radix: string): number {
    const negative = value.startsWith('-');
    const raw = negative ? value.slice(1) : value;
    const parsed = parseInt(raw, this.getBase(radix));

    return negative ? -parsed : parsed;
  }

  private calculate(left: number, right: number, op: '+' | '-' | '*' | '/'): number {
    switch (op) {
      case '+':
        return left + right;
      case '-':
        return left - right;
      case '*':
        return left * right;
      case '/':
        return left / right;
    }
  }

  private formatValue(value: number, radix: 'BIN' | 'OCT' | 'DEC' | 'HEX'): string {
    if (radix === 'DEC') {
      return String(value);
    }

    const sign = value < 0 ? '-' : '';
    const raw = Math.abs(value).toString(this.getBase(radix)).toUpperCase();

    return `${sign}${raw}`;
  }

  private getBase(radix: string): number {
    switch (radix) {
      case 'BIN':
        return 2;
      case 'OCT':
        return 8;
      case 'DEC':
        return 10;
      case 'HEX':
        return 16;
      default:
        return 10;
    }
  }
}
