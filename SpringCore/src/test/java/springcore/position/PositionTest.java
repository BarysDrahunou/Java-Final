package springcore.position;

import org.junit.Before;
import org.junit.Test;
import springcore.currency.Usd;

import static org.junit.Assert.*;

public class PositionTest {

    Position position1;
    Position position2;
    Position position3;

    @Before
    public void init() {
        position1 = new Position("Agent");
        position2 = new Position("Tutor");
        position3 = new Position("Janitor");
    }

    @Test
    public void setAndGetVacancies() {
        assertEquals(0, position1.getVacancies());
        position1.setVacancies(5);
        assertEquals(5, position1.getVacancies());
        position1.setVacancies(10);
        assertEquals(10, position1.getVacancies());
        assertNotEquals(5, position1.getVacancies());
    }

    @Test
    public void setAndGetActiveWorkers() {
        assertEquals(0, position2.getActiveWorkers());
        position2.setActiveWorkers(3);
        assertEquals(3, position2.getActiveWorkers());
        position2.setActiveWorkers(7);
        assertEquals(7, position2.getActiveWorkers());
        assertNotEquals(3, position2.getActiveWorkers());
    }

    @Test
    public void setAndGetSalary() {
        Usd salary = new Usd(0);
        Usd salary1 = new Usd(75);
        Usd salary2 = new Usd(150);
        assertEquals(salary, position1.getSalary());
        position1.setSalary(salary1);
        assertEquals(salary1, position1.getSalary());
        position1.setSalary(salary2);
        assertEquals(salary2, position1.getSalary());
        assertNotEquals(salary, position1.getSalary());
    }

    @Test
    public void getPositionName() {
        assertEquals("Agent", position1.getPositionName());
        assertEquals("Tutor", position2.getPositionName());
        assertEquals("Janitor", position3.getPositionName());
        assertNotEquals("Janitor", position1.getPositionName());
    }

    @Test
    public void testEquals() {
        assertNotEquals(position1, position2);
        assertNotEquals(position2, position3);
        assertNotEquals(position1, position3);
        assertEquals(position1, new Position("Agent"));
    }

    @Test
    public void testHashCode() {
        assertNotEquals(position1.hashCode(), position2.hashCode());
        assertNotEquals(position2.hashCode(), position3.hashCode());
        assertNotEquals(position1.hashCode(), position3.hashCode());
        assertEquals(position1.hashCode(), new Position("Agent").hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("Agent", position1.toString());
        assertEquals("Tutor", position2.toString());
        assertEquals("Janitor", position3.toString());
        assertNotEquals("Janitor", position1.toString());
    }
}