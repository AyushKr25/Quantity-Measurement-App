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

    public Length add(Length thatLength) {
        if (thatLength == null || thatLength.unit == null) {
            throw new IllegalArgumentException("Length to add cannot be null");
        }
        double sumInInches = this.convertToBaseUnit() + thatLength.convertToBaseUnit();
        return new Length(this.convertFromBaseToTargetUnit(sumInInches, this.unit), this.unit);
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

    public static void main(String[] args) {
        System.out.println("--- Addition Demonstrations ---");

        Length length1 = new Length(1.0, LengthUnit.FEET);
        Length length2 = new Length(12.0, LengthUnit.INCHES);
        Length sum1 = demonstrateLengthAddition(length1, length2);
        System.out.println("1.0 FEET + 12.0 INCHES -> " + sum1.toString());

        Length length3 = new Length(12.0, LengthUnit.INCHES);
        Length length4 = new Length(1.0, LengthUnit.FEET);
        Length sum2 = demonstrateLengthAddition(length3, length4);
        System.out.println("12.0 INCHES + 1.0 FEET -> " + sum2.toString());

        Length length5 = new Length(1.0, LengthUnit.YARDS);
        Length length6 = new Length(3.0, LengthUnit.FEET);
        Length sum3 = demonstrateLengthAddition(length5, length6);
        System.out.println("1.0 YARDS + 3.0 FEET -> " + sum3.toString());

        Length length7 = new Length(2.54, LengthUnit.CENTIMETERS);
        Length length8 = new Length(1.0, LengthUnit.INCHES);
        Length sum4 = demonstrateLengthAddition(length7, length8);
        System.out.println("2.54 CENTIMETERS + 1.0 INCHES -> " + sum4.toString());
    }
}