import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-input-field',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './input-field.component.html',
  styleUrl: './input-field.component.scss',
})
export class InputFieldComponent {
  @Input() label = '';
  @Input() value = '';
  @Input() radix = 'DEC';
  @Input() disabled = false;
  @Input() forbidZero = false;
  @Input() inputTestId = '';

  @Output() valueChange = new EventEmitter<string>();

  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const result = this.sanitizeValue(input.value, this.radix, this.forbidZero);

    input.value = result;
    this.valueChange.emit(result);
  }

  sanitizeValue(
    value: string,
    radix: string = this.radix,
    forbidZero: boolean = this.forbidZero,
  ): string {
    let next = value.toUpperCase().replace(/[^0-9A-F\-]/g, '');

    if (next.includes('-')) {
      next = (next.startsWith('-') ? '-' : '') + next.replace(/-/g, '');
    }

    const allowed = this.getAllowedPattern(radix);
    const sign = next.startsWith('-') ? '-' : '';
    const raw = sign ? next.slice(1) : next;
    const result = sign + [...raw].filter((char) => allowed.test(char)).join('');

    if (forbidZero && this.isZeroValue(result)) {
      return '';
    }

    return result;
  }

  private getAllowedPattern(radix: string): RegExp {
    switch (radix) {
      case 'BIN':
        return /^[01]$/;
      case 'OCT':
        return /^[0-7]$/;
      case 'DEC':
        return /^[0-9]$/;
      case 'HEX':
        return /^[0-9A-F]$/;
      default:
        return /^[0-9]$/;
    }
  }

  private isZeroValue(value: string): boolean {
    return /^-?0+$/.test(value.trim());
  }
}
