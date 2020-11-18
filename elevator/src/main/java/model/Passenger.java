package model;

import java.util.Collections;
import java.util.List;
import java.util.stream.*;

import static constants.Constants.*;

public class Passenger {

    private static int id = INITIAL_ID;
    private final int personalId;
    private final int sourceFloor;
    private final int destinationFloor;
    private final MovementDirection movementDirection;

    private TransportationState transportationState;

    public Passenger(int floorsNumber) {
        this.personalId = id++;
        List<Integer> randomFloors = getRandomFloors(floorsNumber);
        this.sourceFloor = randomFloors.get(0);
        this.destinationFloor = randomFloors.get(randomFloors.size() - 1);
        this.transportationState = TransportationState.NOT_STARTED;
        this.movementDirection = destinationFloor > sourceFloor ?
                MovementDirection.UP : MovementDirection.DOWN;
    }

    private List<Integer> getRandomFloors(int floorsNumber) {
        List<Integer> randomFloorsArray = IntStream
                .range(INITIAL_FLOOR, floorsNumber + INITIAL_FLOOR)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(randomFloorsArray);
        return randomFloorsArray;
    }

    public void setTransportationState(TransportationState transportationState) {
        this.transportationState = transportationState;
    }

    public int getPersonalId() {
        return personalId;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public TransportationState getTransportationState() {
        return transportationState;
    }

    public MovementDirection getMovementDirection() {
        return movementDirection;
    }
}