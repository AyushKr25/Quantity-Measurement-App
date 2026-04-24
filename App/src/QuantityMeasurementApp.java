enum LengthUnit {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return Math.round(value * this.conversionFactor * 100.0) / 100.0;
    }

    public double convertFromBaseUnit(double baseValue) {
        return Math.round((baseValue / this.conversionFactor) * 100.0) / 100.0;
    }
}

class Length {
    private final double value;
    private final LengthUnit unit;

    public Length(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    private double convertToBaseUnit() {
        return this.unit.convertToBaseUnit(this.value);
    }

    public Length convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double inInches = this.convertToBaseUnit();
        return new Length(targetUnit.convertFromBaseUnit(inInches), targetUnit);
    }

    public Length add(Length thatLength) {
        if (thatLength == null || thatLength.unit == null) {
            throw new IllegalArgumentException("Length to add cannot be null");
        }
        return addAndConvert(thatLength, this.unit);
    }

    public Length add(Length length, LengthUnit targetUnit) {
        if (length == null || length.unit == null) {
            throw new IllegalArgumentException("Length to add cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        return addAndConvert(length, targetUnit);
    }

    private Length addAndConvert(Length length, LengthUnit targetUnit) {
        double sumInInches = this.convertToBaseUnit() + length.convertToBaseUnit();
        return new Length(targetUnit.convertFromBaseUnit(sumInInches), targetUnit);
    }

    public boolean compare(Length thatLength) {
        if (thatLength == null || thatLength.unit == null) {
            return false;
        }
        return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length length = (Length) o;
        return compare(length);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit.name());
    }
}

public class QuantityMeasurementApp {

    public static boolean demonstrateLengthEquality(Length length1, Length length2) {
        return length1.equals(length2);
    }

    public static boolean demonstrateLengthComparison(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {
        Length length1 = new Length(value1, unit1);
        Length length2 = new Length(value2, unit2);
        boolean result = length1.equals(length2);
        System.out.println("Are lengths equal? " + result);
        return result;
    }

    public static Length demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        Length length = new Length(value, fromUnit);
        return length.convertTo(toUnit);
    }

    public static Length demonstrateLengthConversion(Length length, LengthUnit toUnit) {
        return length.convertTo(toUnit);
    }

    public static Length demonstrateLengthAddition(Length length1, Length length2) {
        return length1.add(length2);
    }

    public static Length demonstrateLengthAddition(Length length1, Length length2, LengthUnit targetUnit) {
        return length1.add(length2, targetUnit);
    }

    public static void main(String[] args) {
        double baseValue = LengthUnit.FEET.convertToBaseUnit(5.0);
        System.out.println("5.0 FEET to base unit (INCHES): " + baseValue);

        double targetValue = LengthUnit.YARDS.convertFromBaseUnit(108.0);
        System.out.println("108.0 base unit (INCHES) to YARDS: " + targetValue);

        Length length1 = new Length(1.0, LengthUnit.FEET);
        Length length2 = new Length(12.0, LengthUnit.INCHES);
        System.out.println("Equality (1.0 FEET == 12.0 INCHES) -> " + length1.equals(length2));

        Length length3 = new Length(2.0, LengthUnit.YARDS);
        Length converted = length3.convertTo(LengthUnit.INCHES);
        System.out.println("Conversion (2.0 YARDS -> INCHES) -> " + converted);

        Length sum = demonstrateLengthAddition(length1, length2, LengthUnit.YARDS);
        System.out.println("Addition (1.0 FEET + 12.0 INCHES, Target: YARDS) -> " + sum);

        Length cmLength = new Length(2.54, LengthUnit.CENTIMETERS);
        Length convertedCm = cmLength.convertTo(LengthUnit.INCHES);
        System.out.println("Conversion (2.54 CENTIMETERS -> INCHES) -> " + convertedCm);
    }
}