import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { CalculatorComponent } from './calculator.component';

describe('CalculatorComponent', () => {
  let component: CalculatorComponent;
  let fixture: ComponentFixture<CalculatorComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalculatorComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(CalculatorComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render two input components', () => {
    const inputs = fixture.nativeElement.querySelectorAll('app-input-field');
    expect(inputs.length).toBe(2);
  });

  it('should render calculate button', () => {
    const button = fixture.nativeElement.querySelector('button');
    expect(button.textContent.trim()).toContain('Вычислить');
  });

  it('should block division by zero', async () => {
    component.leftValue = '10';
    component.rightValue = '0';
    component.operation = '/';

    await component.calculate();

    expect(component.errorMessage).toBe('При делении второе число не может быть равно 0');
  });

  it('should disable calculate button when division by zero selected', () => {
    component.leftValue = '10';
    component.rightValue = '0';
    component.operation = '/';

    expect(component.isCalculateDisabled()).toBeTrue();
  });

  it('should send request and set result', fakeAsync(() => {
    component.leftValue = '101';
    component.rightValue = 'A';
    component.leftRadix = 'BIN';
    component.rightRadix = 'HEX';
    component.resultRadix = 'DEC';
    component.operation = '+';

    component.calculate();
    tick();

    const req = httpMock.expectOne('http://localhost:8080/api/calc');
    expect(req.request.method).toBe('POST');
    expect(req.request.body.op).toBe('+');

    req.flush({
      id: 1,
      result: '15',
      resultRadix: 'DEC',
      createdAt: '2026-03-10T10:00:00Z',
    });

    tick();
    fixture.detectChanges();

    expect(component.result).toBe('15');
    expect(component.resultDecimalValue).toBe(15);
  }));

  it('should show backend error message', fakeAsync(() => {
    component.leftValue = '10';
    component.rightValue = '1';
    component.leftRadix = 'DEC';
    component.rightRadix = 'DEC';
    component.resultRadix = 'DEC';
    component.operation = '+';

    component.calculate();
    tick();

    const req = httpMock.expectOne('http://localhost:8080/api/calc');
    req.flush({ message: 'backend error' }, { status: 400, statusText: 'Bad Request' });

    tick();

    expect(component.errorMessage).toBe('backend error');
  }));

  it('should sanitize left value when radix changes', () => {
    component.leftValue = '12AF';

    component.setLeftRadix('BIN');

    expect(component.leftValue).toBe('1');
  });

  it('should clear right value when switching to division with zero', () => {
    component.rightValue = '0';
    component.operation = '/';

    component.onOperationChange();

    expect(component.rightValue).toBe('');
  });
});
