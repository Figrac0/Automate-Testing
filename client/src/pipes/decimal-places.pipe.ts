import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'decimalPlaces',
  standalone: true,
})
export class DecimalPlacesPipe implements PipeTransform {
  transform(value: string | number | null | undefined, places: number = 2): string {
    if (value === null || value === undefined || value === '') {
      return '';
    }

    const parsed = Number(value);

    if (Number.isNaN(parsed)) {
      return String(value);
    }

    return parsed.toFixed(places);
  }
}
