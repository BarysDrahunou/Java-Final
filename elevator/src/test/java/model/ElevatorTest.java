package model;


import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static constants.Constants.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElevatorTest {
    Elevator elevator;
    Elevator elevator1;
    @Mock
    Passenger passenger1;
    @Mock
    Passenger passenger2;
    int floorsNumber1 = 5;
    int floorsNumber2 = 3;

    @Before
    public void setUp() {
        elevator = new Elevator(floorsNumber1, 3,2);
        elevator1 = new Elevator(floorsNumber2, 1,RANDOM_FLOOR_VALUE);
    }

    @Test
    public void hasFreeSpace() {
        assertTrue(elevator.hasFreeSpace());
        assertTrue(elevator1.hasFreeSpace());
        elevator.getContainer().add(passenger1);
        elevator1.getContainer().add(passenger1);
        assertTrue(elevator.hasFreeSpace());
        assertFalse(elevator1.hasFreeSpace());
    }

    @Test
    public void setCurrentFloor() {
        elevator.setCurrentFloorNumber(1);
        assertNotEquals(3, elevator.getCurrentFloorNumber());
        assertEquals(1, elevator.getCurrentFloorNumber());
        elevator1.setCurrentFloorNumber(2);
        assertNotEquals(1, elevator1.getCurrentFloorNumber());
        assertEquals(2, elevator1.getCurrentFloorNumber());
    }

    @Test
    public void setMovementDirection() {
        elevator.setMovementDirection(MovementDirection.DOWN);
        assertNotEquals(MovementDirection.UP, elevator.getMovementDirection());
        assertEquals(MovementDirection.DOWN, elevator.getMovementDirection());
        elevator1.setMovementDirection(MovementDirection.UP);
        assertEquals(MovementDirection.UP, elevator1.getMovementDirection());
        assertNotEquals(MovementDirection.DOWN, elevator1.getMovementDirection());
    }

    @Test
    public void getCapacity() {
        assertNotEquals(5, elevator.getCapacity());
        assertNotEquals(0, elevator1.getCapacity());
        assertEquals(3, elevator.getCapacity());
        assertEquals(1, elevator1.getCapacity());
    }

    @Test
    public void getContainer() {
        assertEquals(new ArrayList<>(), elevator.getContainer());
        assertEquals(0, elevator.getContainer().size());
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger1);
        passengers.add(passenger2);
        elevator.getContainer().add(passenger1);
        elevator.getContainer().add(passenger2);
        assertEquals(passengers, elevator.getContainer());
        elevator.getContainer().remove(0);
        passengers.remove(passenger1);
        assertEquals(passengers, elevator.getContainer());
    }

    @Test
    public void getCurrentFloorNumber() {
        assertTrue(IntStream.range(0, 100)
                .mapToObj(x -> new Elevator(floorsNumber1, 3,RANDOM_FLOOR_VALUE))
                .allMatch(elevator -> elevator.getCurrentFloorNumber() <= floorsNumber1));
        assertTrue(IntStream.range(0, 100)
                .mapToObj(x -> new Elevator(floorsNumber2, 3,RANDOM_FLOOR_VALUE))
                .allMatch(elevator -> elevator.getCurrentFloorNumber() <= floorsNumber2));
        assertTrue(elevator.getCurrentFloorNumber() > 0);
        assertTrue(elevator1.getCurrentFloorNumber() > 0);
    }

    @Test
    public void aGetPersonalId() {
        assertEquals(1, elevator.getPersonalId());
        assertEquals(2, elevator1.getPersonalId());
        assertNotEquals(2, elevator.getPersonalId());
    }

    @Test
    public void getMovementDirection() {
        elevator.setMovementDirection(MovementDirection.DOWN);
        assertNotEquals(MovementDirection.UP, elevator.getMovementDirection());
        assertEquals(MovementDirection.DOWN, elevator.getMovementDirection());
    }
}