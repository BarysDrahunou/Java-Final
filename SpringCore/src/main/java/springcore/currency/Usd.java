package springcore.currency;

import java.math.*;
import java.util.*;

/**
 * The type Usd.
 */
public final class Usd {

    private final int value;

    /**
     * Instantiates a new Usd.
     *
     * @param value the value for instantiating new Usd
     */
    public Usd(int value) {

        this.value = value;
    }

    /**
     * Addition usd.
     *
     * @param usd the argument
     * @return Returns the sum of the value and the argument
     */
    public Usd addition(Usd usd) {
        return new Usd(value + usd.value);
    }

    /**
     * Multiplication usd.
     *
     * @param k the argument
     * @return Returns the product of the value and the argument
     */
    public Usd multiplication(int k) {
        return new Usd(value * k);
    }

    /**
     * Division usd.
     *
     * @param divisor the argument
     * @return Returns the result of division of the value and the argument
     */
    public Usd division(int divisor) {
        return new Usd(value / divisor);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usd usd = (Usd) o;
        return value == usd.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return new BigDecimal(BigInteger.valueOf(value))
                .movePointLeft(2).toString();
    }
}
