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
}

class Length {
    private final double value;
    private final LengthUnit unit;

    public Length(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    private double convertToBaseUnit() {
        double inInches = this.value * this.unit.getConversionFactor();
        return Math.round(inInches * 100.0) / 100.0;
    }

    public Length convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double inInches = this.convertToBaseUnit();
        double targetValue = inInches / targetUnit.getConversionFactor();
        return new Length(Math.round(targetValue * 100.0) / 100.0, targetUnit);
    }

    public boolean compare(Length thatLength) {
        if (thatLength == null || this.unit == null || thatLength.unit == null) {
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

    public static void main(String[] args) {
        // UC4: Equality and Comparison Demonstrations
        System.out.println("--- Equality Comparisons ---");
        demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
        demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCHES);
        demonstrateLengthComparison(100.0, LengthUnit.CENTIMETERS, 39.3701, LengthUnit.INCHES);

        // UC5: Explicit Unit-to-Unit Conversion Demonstrations
        System.out.println("\n--- Unit Conversions ---");

        // 1. Using raw values
        Length converted1 = demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        System.out.println("1.0 FEET to INCHES -> " + converted1.toString());

        Length converted2 = demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        System.out.println("36.0 INCHES to YARDS -> " + converted2.toString());

        // 2. Using an existing Length instance
        Length lengthInYards = new Length(2.0, LengthUnit.YARDS);
        Length converted3 = demonstrateLengthConversion(lengthInYards, LengthUnit.INCHES);
        System.out.println("2.0 YARDS to INCHES -> " + converted3.toString());

        Length lengthInCm = new Length(2.54, LengthUnit.CENTIMETERS);
        Length converted4 = demonstrateLengthConversion(lengthInCm, LengthUnit.INCHES);
        System.out.println("2.54 CENTIMETERS to INCHES -> " + converted4.toString());
    }
}