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

    private double convertFromBaseToTargetUnit(double lengthInInches, LengthUnit targetUnit) {
        double targetValue = lengthInInches / targetUnit.getConversionFactor();
        return Math.round(targetValue * 100.0) / 100.0;
    }

    public Length convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double inInches = this.convertToBaseUnit();
        return new Length(this.convertFromBaseToTargetUnit(inInches, targetUnit), targetUnit);
    }

    // UC6: Addition where result defaults to the unit of the first operand
    public Length add(Length thatLength) {
        if (thatLength == null || thatLength.unit == null) {
            throw new IllegalArgumentException("Length to add cannot be null");
        }
        return addAndConvert(thatLength, this.unit);
    }

    // UC7: Overloaded addition with explicitly specified target unit
    public Length add(Length length, LengthUnit targetUnit) {
        if (length == null || length.unit == null) {
            throw new IllegalArgumentException("Length to add cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        return addAndConvert(length, targetUnit);
    }

    // UC7: Private utility method to perform addition conversion
    private Length addAndConvert(Length length, LengthUnit targetUnit) {
        double sumInInches = this.convertToBaseUnit() + length.convertToBaseUnit();
        return new Length(this.convertFromBaseToTargetUnit(sumInInches, targetUnit), targetUnit);
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

    public static Length demonstrateLengthAddition(Length length1, Length length2) {
        return length1.add(length2);
    }

    // UC7: Overloaded demonstrate method for explicit target unit
    public static Length demonstrateLengthAddition(Length length1, Length length2, LengthUnit targetUnit) {
        return length1.add(length2, targetUnit);
    }

    public static void main(String[] args) {
        System.out.println("--- UC6: Addition (Implicit Target Unit) ---");
        Length length1 = new Length(1.0, LengthUnit.FEET);
        Length length2 = new Length(12.0, LengthUnit.INCHES);
        System.out.println("1.0 FEET + 12.0 INCHES -> " + demonstrateLengthAddition(length1, length2));

        System.out.println("\n--- UC7: Addition (Explicit Target Unit) ---");

        Length sumFeet = demonstrateLengthAddition(length1, length2, LengthUnit.FEET);
        System.out.println("1.0 FEET + 12.0 INCHES (Target: FEET) -> " + sumFeet);

        Length sumInches = demonstrateLengthAddition(length1, length2, LengthUnit.INCHES);
        System.out.println("1.0 FEET + 12.0 INCHES (Target: INCHES) -> " + sumInches);

        Length sumYards = demonstrateLengthAddition(length1, length2, LengthUnit.YARDS);
        System.out.println("1.0 FEET + 12.0 INCHES (Target: YARDS) -> " + sumYards);

        Length l3 = new Length(1.0, LengthUnit.YARDS);
        Length l4 = new Length(3.0, LengthUnit.FEET);
        Length sumYards2 = demonstrateLengthAddition(l3, l4, LengthUnit.YARDS);
        System.out.println("1.0 YARDS + 3.0 FEET (Target: YARDS) -> " + sumYards2);

        Length l5 = new Length(36.0, LengthUnit.INCHES);
        Length l6 = new Length(1.0, LengthUnit.YARDS);
        Length sumFeet2 = demonstrateLengthAddition(l5, l6, LengthUnit.FEET);
        System.out.println("36.0 INCHES + 1.0 YARDS (Target: FEET) -> " + sumFeet2);

        Length l7 = new Length(2.54, LengthUnit.CENTIMETERS);
        Length l8 = new Length(1.0, LengthUnit.INCHES);
        Length sumInches2 = demonstrateLengthAddition(l7, l8, LengthUnit.INCHES);
        System.out.println("2.54 CENTIMETERS + 1.0 INCHES (Target: INCHES) -> " + sumInches2);

        Length l9 = new Length(5.0, LengthUnit.FEET);
        Length l10 = new Length(-2.0, LengthUnit.FEET);
        Length sumInches3 = demonstrateLengthAddition(l9, l10, LengthUnit.INCHES);
        System.out.println("5.0 FEET + (-2.0) FEET (Target: INCHES) -> " + sumInches3);
    }
}