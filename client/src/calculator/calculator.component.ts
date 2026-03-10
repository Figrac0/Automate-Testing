import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

import { ResultColorDirective } from '../directives/result-color.directive';
import { InputFieldComponent } from '../input-field/input-field.component';
import { DecimalPlacesPipe } from '../pipes/decimal-places.pipe';

type Radix = 'BIN' | 'OCT' | 'DEC' | 'HEX';
type Operation = '+' | '-' | '*' | '/';

interface CalcResponse {
  id: number;
  result: string;
  resultRadix: string;
  createdAt: string;
}

@Component({
  selector: 'app-calculator',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InputFieldComponent,
    ResultColorDirective,
    DecimalPlacesPipe,
  ],
  templateUrl: './calculator.component.html',
  styleUrl: './calculator.component.scss',
})
export class CalculatorComponent {
  leftValue = '';
  rightValue = '';
  operation: Operation = '+';

  leftRadix: Radix = 'DEC';
  rightRadix: Radix = 'DEC';
  resultRadix: Radix = 'DEC';

  result = '';
  resultDecimalValue: number | null = null;
  errorMessage = '';
  loading = false;

  readonly radixOptions: Radix[] = ['BIN', 'OCT', 'DEC', 'HEX'];
  readonly operationOptions: { label: string; value: Operation }[] = [
    { label: 'Сложить', value: '+' },
    { label: 'Вычесть', value: '-' },
    { label: 'Умножить', value: '*' },
    { label: 'Разделить', value: '/' },
  ];

  constructor(private http: HttpClient) {}

  setLeftValue(value: string): void {
    this.leftValue = value;
  }

  setRightValue(value: string): void {
    if (this.operation === '/' && this.normalizeZero(value)) {
      return;
    }

    this.rightValue = value;
  }

  setLeftRadix(radix: Radix): void {
    this.leftRadix = radix;
    this.leftValue = this.sanitizeValue(this.leftValue, radix);
  }

  setRightRadix(radix: Radix): void {
    this.rightRadix = radix;
    this.rightValue = this.sanitizeValue(this.rightValue, radix);

    if (this.operation === '/' && this.normalizeZero(this.rightValue)) {
      this.rightValue = '';
    }
  }

  async calculate(): Promise<void> {
    this.errorMessage = '';
    this.result = '';
    this.resultDecimalValue = null;

    if (!this.leftValue || !this.rightValue) {
      this.errorMessage = 'Оба числа должны быть заполнены';
      return;
    }

    if (this.operation === '/' && this.normalizeZero(this.rightValue)) {
      this.errorMessage = 'При делении второе число не может быть равно 0';
      return;
    }

    const body = {
      left: {
        value: this.leftValue,
        radix: this.leftRadix,
      },
      right: {
        value: this.rightValue,
        radix: this.rightRadix,
      },
      op: this.operation,
      resultRadix: this.resultRadix,
    };

    this.loading = true;

    try {
      const response = await firstValueFrom(
        this.http.post<CalcResponse>('http://localhost:8080/api/calc', body),
      );

      this.result = response.result;
      this.resultDecimalValue = this.parseResultToDecimal(response.result, this.resultRadix);
    } catch (error: any) {
      this.errorMessage = error?.error?.message || 'Ошибка запроса к серверу';
    } finally {
      this.loading = false;
    }
  }

  onOperationChange(): void {
    if (this.operation === '/' && this.normalizeZero(this.rightValue)) {
      this.rightValue = '';
    }
  }

  isCalculateDisabled(): boolean {
    return (
      this.loading ||
      !this.leftValue ||
      !this.rightValue ||
      (this.operation === '/' && this.normalizeZero(this.rightValue))
    );
  }

  private normalizeZero(value: string): boolean {
    if (!value) {
      return false;
    }

    return /^-?0+$/.test(value.trim().toUpperCase());
  }

  private sanitizeValue(value: string, radix: Radix): string {
    let next = value.toUpperCase().replace(/[^0-9A-F\-]/g, '');

    if (next.includes('-')) {
      next = (next.startsWith('-') ? '-' : '') + next.replace(/-/g, '');
    }

    const allowed = this.getAllowedPattern(radix);
    const sign = next.startsWith('-') ? '-' : '';
    const raw = sign ? next.slice(1) : next;

    return sign + [...raw].filter((char) => allowed.test(char)).join('');
  }

  private getAllowedPattern(radix: Radix): RegExp {
    switch (radix) {
      case 'BIN':
        return /^[01]$/;
      case 'OCT':
        return /^[0-7]$/;
      case 'DEC':
        return /^[0-9]$/;
      case 'HEX':
        return /^[0-9A-F]$/;
    }
  }

  private parseResultToDecimal(value: string, radix: Radix): number {
    const normalized = value.trim().toUpperCase();

    if (!normalized) {
      return 0;
    }

    const negative = normalized.startsWith('-');
    const raw = negative ? normalized.slice(1) : normalized;
    const parsed = parseInt(raw, this.getBase(radix));

    return negative ? -parsed : parsed;
  }

  private getBase(radix: Radix): number {
    switch (radix) {
      case 'BIN':
        return 2;
      case 'OCT':
        return 8;
      case 'DEC':
        return 10;
      case 'HEX':
        return 16;
    }
  }
}
