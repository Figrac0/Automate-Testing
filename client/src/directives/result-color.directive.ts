import { Directive, ElementRef, Input, OnChanges, Renderer2, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[appResultColor]',
  standalone: true,
})
export class ResultColorDirective implements OnChanges {
  @Input('appResultColor') value: string | number | null = null;

  constructor(
    private el: ElementRef,
    private renderer: Renderer2,
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (!('value' in changes)) {
      return;
    }

    const num = Number(this.value);

    if (Number.isNaN(num)) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'black');
      return;
    }

    if (num < 0) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'red');
    } else if (num === 0) {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'black');
    } else {
      this.renderer.setStyle(this.el.nativeElement, 'color', 'green');
    }
  }
}
