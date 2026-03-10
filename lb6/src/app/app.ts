import { Component } from '@angular/core';
import { CalculatorComponent } from '../calculator/calculator.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CalculatorComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {}
