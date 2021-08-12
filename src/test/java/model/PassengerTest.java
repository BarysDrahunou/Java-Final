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
}