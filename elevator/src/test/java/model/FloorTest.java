package model;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FloorTest {

    Floor floor1;
    Floor floor2;
    @Mock
    Passenger passenger1;
    @Mock
    Passenger passenger2;

    @Before
    public void setUp() {
        floor1 = new Floor(5);
        floor2 = new Floor(1);
    }

    @Test
    public void addPassengerToDispatchContainer() {
        assertEquals(0, floor1.getDispatchContainer().size());
        assertEquals(new ArrayList<>(), floor1.getDispatchContainer());

        floor1.getDispatchContainer().add(passenger1);

        assertEquals(1, floor1.getDispatchContainer().size());

        floor1.getDispatchContainer().add(passenger1);

        assertEquals(2, floor1.getDispatchContainer().size());
    }

    @Test
    public void addPassengerToArrivalContainer() {
        assertEquals(0, floor1.getArrivalContainer().size());
        assertEquals(new ArrayList<>(), floor1.getArrivalContainer());

        floor1.getArrivalContainer().add(passenger1);

        assertEquals(1, floor1.getArrivalContainer().size());

        floor1.getArrivalContainer().add(passenger1);

        assertEquals(2, floor1.getArrivalContainer().size());
    }

    @Test
    public void getDispatchContainer() {
        floor2.addPassengerToDispatchContainer(passenger1);
        floor2.addPassengerToDispatchContainer(passenger2);

        assertEquals(passenger1, floor2.getDispatchContainer().get(0));
        assertEquals(passenger2, floor2.getDispatchContainer().get(1));

        floor2.getDispatchContainer().clear();

        assertEquals(0, floor2.getDispatchContainer().size());
    }

    @Test
    public void getArrivalContainer() {
        floor2.addPassengerToArrivalContainer(passenger1);
        floor2.addPassengerToArrivalContainer(passenger2);

        assertEquals(passenger1, floor2.getArrivalContainer().get(0));
        assertEquals(passenger2, floor2.getArrivalContainer().get(1));

        floor2.getArrivalContainer().clear();

        assertEquals(0, floor2.getArrivalContainer().size());
    }
}