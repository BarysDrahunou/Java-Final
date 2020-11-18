package springcore.currency;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UsdTest {

    Usd usd1;
    Usd usd2;
    Usd usd3;

    @Before
    public void setUp() {

        usd1 = new Usd(500);
        usd2 = new Usd(100);
        usd3 = new Usd(0);
    }

    @Test
    public void addition() {
        assertEquals(usd1, usd1.addition(usd3));
        assertEquals(usd1, usd2.addition(usd2).addition(usd2).addition(usd2).addition(usd2));
        assertNotEquals(usd2, usd1.addition(usd2));
    }

    @Test
    public void multiplication() {
        assertEquals(usd1, usd2.multiplication(5));
        assertEquals(usd3, usd2.multiplication(0));
        assertNotEquals(usd3, usd2.multiplication(usd1.getValue()));
    }

    @Test
    public void division() {
        assertEquals(usd2, usd1.division(5));
        assertEquals(usd3, usd3.division(100));
        assertNotEquals(usd3, usd1.division(usd2.getValue()));
    }

    @Test
    public void getValue() {
        assertEquals(500, usd1.getValue());
        assertEquals(100, usd2.getValue());
        assertNotEquals(1, usd3.getValue());
    }

    @Test
    public void testEquals() {
        assertEquals(usd1, usd1);
        assertEquals(usd1, usd2.multiplication(5));
        assertNotEquals(usd1, usd3);
        assertEquals(usd1, new Usd(400).addition(usd2));
    }

    @Test
    public void testHashCode() {
        assertEquals(usd1.hashCode(), usd1.hashCode());
        assertEquals(usd1.hashCode(), usd2.multiplication(5).hashCode());
        assertNotEquals(usd1.hashCode(), usd3.hashCode());
        assertEquals(usd1.hashCode(), new Usd(400).addition(usd2).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(usd1.toString(), usd1.toString());
        assertEquals(usd1.toString(), "5.00");
        assertEquals(usd2.toString(), "1.00");
        assertEquals(usd3.toString(), "0.00");
        assertEquals(new Usd(75).toString(), "0.75");
        assertEquals(new Usd(1).multiplication(150).toString(), "1.50");
    }
}