import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ResultColorDirective } from './result-color.directive';

@Component({
  standalone: true,
  imports: [ResultColorDirective],
  template: `<div [appResultColor]="value">Result</div>`,
})
class TestHostComponent {
  value: string | number = 0;
}

describe('ResultColorDirective', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let component: TestHostComponent;
  let div: HTMLDivElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    div = fixture.nativeElement.querySelector('div');
  });

  it('should set red color for negative value', () => {
    component.value = -5;
    fixture.detectChanges();
    expect(div.style.color).toBe('red');
  });

  it('should set black color for zero', () => {
    component.value = 0;
    fixture.detectChanges();
    expect(div.style.color).toBe('black');
  });

  it('should set green color for positive value', () => {
    component.value = 10;
    fixture.detectChanges();
    expect(div.style.color).toBe('green');
  });
});
