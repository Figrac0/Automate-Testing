import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InputFieldComponent } from './input-field.component';

describe('InputFieldComponent', () => {
  let component: InputFieldComponent;
  let fixture: ComponentFixture<InputFieldComponent>;
  let input: HTMLInputElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InputFieldComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(InputFieldComponent);
    component = fixture.componentInstance;
    component.label = 'Число';
    fixture.detectChanges();

    input = fixture.nativeElement.querySelector('input');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render label', () => {
    const label = fixture.nativeElement.querySelector('label');
    expect(label.textContent?.trim()).toBe('Число');
  });

  it('should allow only binary digits for BIN', () => {
    component.radix = 'BIN';
    fixture.detectChanges();

    input.value = '1201ABC';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(input.value).toBe('101');
  });

  it('should allow octal digits for OCT', () => {
    component.radix = 'OCT';
    fixture.detectChanges();

    input.value = '789123';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(input.value).toBe('7123');
  });

  it('should allow decimal digits for DEC', () => {
    component.radix = 'DEC';
    fixture.detectChanges();

    input.value = '12AB34';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(input.value).toBe('1234');
  });

  it('should allow hex digits for HEX', () => {
    component.radix = 'HEX';
    fixture.detectChanges();

    input.value = '1G2ZAF';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(input.value).toBe('12AF');
  });

  it('should support negative numbers', () => {
    component.radix = 'DEC';
    fixture.detectChanges();

    input.value = '-12AB3';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(input.value).toBe('-123');
  });

  it('should clear zero when zero is forbidden', () => {
    component.radix = 'DEC';
    component.forbidZero = true;
    fixture.detectChanges();

    input.value = '0';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(input.value).toBe('');
  });
});
