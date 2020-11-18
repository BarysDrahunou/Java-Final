package springcore.currency;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public final class Usd {

    private final int value;

    public Usd(int value) {
        this.value = value;
    }

    public Usd addition(Usd usd) {
        return new Usd(value + usd.value);
    }

    public Usd multiplication(int k) {
        return new Usd(value * k);
    }

    public Usd division(int divisor) {
        return new Usd(value / divisor);
    }

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
        return new BigDecimal(BigInteger.valueOf(value)).movePointLeft(2).toString();
    }
}
