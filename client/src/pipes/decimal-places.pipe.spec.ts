import { DecimalPlacesPipe } from './decimal-places.pipe';

describe('DecimalPlacesPipe', () => {
  let pipe: DecimalPlacesPipe;

  beforeEach(() => {
    pipe = new DecimalPlacesPipe();
  });

  it('should create', () => {
    expect(pipe).toBeTruthy();
  });

  it('should format with 2 decimal places', () => {
    expect(pipe.transform(12.3456, 2)).toBe('12.35');
  });

  it('should format with 0 decimal places', () => {
    expect(pipe.transform(12.8, 0)).toBe('13');
  });

  it('should return empty string for null', () => {
    expect(pipe.transform(null, 2)).toBe('');
  });

  it('should return source value if NaN', () => {
    expect(pipe.transform('ABC', 2)).toBe('ABC');
  });
});
