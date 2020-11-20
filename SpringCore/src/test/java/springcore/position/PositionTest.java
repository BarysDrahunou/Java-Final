package springcore.position;

import org.junit.Before;
import org.junit.Test;

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
}