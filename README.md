# Angular Calculator with Numeral System Support and Spring Boot Integration

## Project Overview
This project implements a calculator web application with a frontend built in Angular and a backend powered by Spring Boot. The application supports arithmetic operations across multiple numeral systems (binary, octal, decimal, hexadecimal) with proper input validation and result formatting.

## Technology Stack
- **Frontend:** Angular (Standalone Components)
- **Backend:** Spring Boot (from Laboratory Work No. 2)
- **Testing:** Jasmine, Karma (Unit Tests)
- **HTTP Communication:** Angular HttpClient

## Core Features

### 1. Arithmetic Operations
- Addition (+)
- Subtraction (-)
- Multiplication (*)
- Division (/)

### 2. Numeral System Support
- Binary (BIN)
- Octal (OCT)
- Decimal (DEC)
- Hexadecimal (HEX)

### 3. Input Validation
- Character filtering based on selected numeral system
- Zero prohibition in divisor during division operations
- Negative number support

### 4. Result Formatting
- Color-coded results based on sign (red for negative, black for zero, green for positive)
- Configurable decimal places display

## Backend Integration
The application communicates with the Spring Boot backend through a RESTful API:

- **Endpoint:** `POST http://localhost:8080/api/calc`

### Request Format:
```json
{
  "left": { "value": "101", "radix": "BIN" },
  "right": { "value": "A", "radix": "HEX" },
  "op": "+",
  "resultRadix": "DEC"
}
```

## Response Format

```json
{
  "id": 1,
  "result": "15",
  "resultRadix": "DEC",
  "createdAt": "2026-03-10T10:00:00Z"
}
```
## Application Architecture

### Component Structure

```text
src/
├── app/
│ ├── app.ts # Root component
│ ├── app.config.ts # Application configuration
│ └── app.html # Root template
├── calculator/
│ ├── calculator.component.ts # Main calculator logic
│ └── calculator.component.html
├── input-field/
│ ├── input-field.component.ts # Reusable input component
│ └── input-field.component.html
├── directives/
│ └── result-color.directive.ts # Result color directive
└── pipes/
└── decimal-places.pipe.ts # Decimal formatting pipe
```

## Key Components

### Calculator Component
The main orchestrator that manages application state, handles user input, communicates with the backend, and processes results.

**Core Properties:**
- `leftValue`, `rightValue`: User input strings
- `operation`: Selected arithmetic operation
- `leftRadix`, `rightRadix`, `resultRadix`: Numeral system selections
- `result`: Backend response in selected numeral system
- `resultDecimalValue`: Decimal conversion for formatting
- `errorMessage`: Error display text
- `loading`: HTTP request status

### Input Field Component
A reusable component that encapsulates input validation logic based on numeral systems.

**Input Parameters:**
- `label`: Field label
- `value`: Current value
- `radix`: Numeral system
- `disabled`: Field availability
- `forbidZero`: Zero prohibition flag

**Core Functionality:**
- Real-time character filtering
- Numeral system-specific validation
- Minus sign normalization
- Zero value clearing when prohibited

### Result Color Directive
A custom directive that applies color styling to result elements based on their numeric value.

**Color Mapping:**
- Negative values → Red
- Zero → Black
- Positive values → Green

### Decimal Places Pipe
A custom pipe for formatting numeric results with specified decimal precision.

**Features:**
- Configurable decimal places (default: 2)
- Empty value handling
- Non-numeric value passthrough

## Input Validation Rules

### Allowed Characters by Numeral System
| System | Allowed Characters |
|--------|-------------------|
| BIN | 0, 1, - |
| OCT | 0-7, - |
| DEC | 0-9, - |
| HEX | 0-9, A-F, - |

### Special Validation Rules
- Division by zero is prevented
- Zero cannot be entered in the second input during division
- Negative numbers supported across all numeral systems
- Multiple minus signs are normalized to a single leading minus

## Calculation Workflow
1. User enters values and selects numeral systems
2. Input is validated in real-time
3. User selects operation and clicks Calculate
4. Frontend constructs JSON request
5. HTTP POST request sent to backend
6. Backend performs calculation and returns result
7. Result is displayed in selected numeral system
8. Result is converted to decimal for formatting
9. Color directive and decimal pipe are applied

## Unit Testing

### Test Coverage

**App Component**
- Component creation
- Calculator component presence in template

**Calculator Component**
- Component initialization
- UI element verification
- Division by zero prevention
- Button disable state
- HTTP request handling
- Response processing
- Error handling
- Numeral system change effects

**Input Field Component**
- Label rendering
- Numeral system validation
- Negative number support
- Zero prohibition functionality

**Result Color Directive**
- Color application for all value ranges
- NaN handling

**Decimal Places Pipe**
- Formatting with various decimal places
- Edge case handling (null, undefined, non-numeric)

### Testing Tools
- **Jasmine:** Testing framework
- **Karma:** Test runner
- **HttpTestingController:** HTTP request mocking

## Getting Started

### Prerequisites
- Node.js and npm
- Angular CLI
- Spring Boot backend running on localhost:8080

## Dependencies
- `@angular/common`: HttpClient for HTTP communication
- `@angular/core`: Core Angular functionality
- `@angular/platform-browser`: Browser platform
- `rxjs`: Reactive extensions for async operations

## Key Implementation Details

### HTTP Communication

```typescript
const response = await firstValueFrom(
  this.http.post<CalcResponse>('http://localhost:8080/api/calc', body)
);
```

### Input Sanitization

```typescript
sanitizeValue(value: string, radix: string): string {
  // Remove invalid characters
  let next = value.toUpperCase().replace(/[^0-9A-F\-]/g, '');
  
  // Normalize minus sign
  if (next.includes('-')) {
    next = (next.startsWith('-') ? '-' : '') + next.replace(/-/g, '');
  }
  
  // Filter by numeral system
  const allowed = this.getAllowedPattern(radix);
  const result = [...raw].filter((char) => allowed.test(char)).join('');
  
  return sign + result;
}
```

### Result Color Application

```typescript
@Directive({
  selector: '[appResultColor]'
})
export class ResultColorDirective implements OnChanges {
  ngOnChanges(changes: SimpleChanges): void {
    const num = Number(this.value);
    
    if (num < 0) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'red');
    } else if (num === 0) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'black');
    } else {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'green');
    }
  }
}
```
### Decimal Formatting Pipe

```typescript
@Pipe({ name: 'decimalPlaces' })
export class DecimalPlacesPipe implements PipeTransform {
  transform(value: number | null, places: number = 2): string {
    if (value === null) return '';
    return value.toFixed(places);
  }
}
```

## Conclusion

This Angular calculator demonstrates a complete full-stack implementation with proper separation of concerns, reusable components, comprehensive input validation, and thorough unit testing. The application successfully integrates with a Spring Boot backend while providing a responsive and user-friendly interface for arithmetic operations across multiple numeral systems.



