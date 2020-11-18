package model;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PassengerTest {

    Passenger passenger1;
    Passenger passenger2;
    Passenger passenger3;
    int floorsNumber1 = 5;
    int floorsNumber2 = 3;

    @Before
    public void setUp() {
        passenger1 = new Passenger(floorsNumber1);
        passenger2 = new Passenger(floorsNumber2);
        passenger3 = new Passenger(1);
    }

    @Test
    public void setAndGetTransportationState() {
        passenger1.setTransportationState(TransportationState.IN_PROGRESS);
        passenger2.setTransportationState(TransportationState.COMPLETED);
        passenger3.setTransportationState(TransportationState.COMPLETED);
        assertEquals(passenger2.getTransportationState(), passenger3.getTransportationState());
        assertNotEquals(passenger1.getTransportationState(), passenger3.getTransportationState());
        assertNotEquals(passenger1.getTransportationState(), passenger2.getTransportationState());
    }

    @Test
    public void aGetPersonalId() {
        assertEquals(1, passenger1.getPersonalId());
        assertEquals(2, passenger2.getPersonalId());
        assertEquals(3, passenger3.getPersonalId());
    }

    @Test
    public void getSourceFloor() {
        assertTrue(IntStream.range(0, 100)
                .mapToObj(x -> new Passenger(floorsNumber1))
                .allMatch(passenger -> passenger.getSourceFloor() <= floorsNumber1));
        assertTrue(IntStream.range(0, 100)
                .mapToObj(x -> new Passenger(floorsNumber2))
                .allMatch(passenger -> passenger.getSourceFloor() <= floorsNumber2));
        assertTrue(passenger1.getSourceFloor() > 0);
        assertTrue(passenger2.getSourceFloor() > 0);
    }

    @Test
    public void getDestinationFloor() {
        assertTrue(IntStream.range(0, 100)
                .mapToObj(x -> new Passenger(floorsNumber1))
                .allMatch(passenger -> passenger.getDestinationFloor() <= floorsNumber1));
        assertTrue(IntStream.range(0, 100)
                .mapToObj(x -> new Passenger(floorsNumber2))
                .allMatch(passenger -> passenger.getDestinationFloor() <= floorsNumber2));
        assertTrue(passenger1.getDestinationFloor() > 0);
        assertTrue(passenger2.getDestinationFloor() > 0);
    }


    @Test
    public void getMovementDirection() {
        assertTrue(passenger1.getMovementDirection() == MovementDirection.DOWN
                || passenger1.getMovementDirection() == MovementDirection.UP);
        assertTrue(passenger2.getMovementDirection() == MovementDirection.DOWN
                || passenger2.getMovementDirection() == MovementDirection.UP);
        assertTrue(passenger3.getMovementDirection() == MovementDirection.DOWN
                || passenger3.getMovementDirection() == MovementDirection.UP);
    }
}