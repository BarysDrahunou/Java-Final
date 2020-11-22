package task;

import controller.*;
import model.*;


public class PassengerTransportationTask implements Runnable {

    private final Passenger passenger;
    private final Controller controller;

    public PassengerTransportationTask(Passenger passenger, Controller controller) {
        this.passenger = passenger;
        this.controller = controller;

        passenger.setTransportationState(TransportationState.IN_PROGRESS);
    }

    public void run() {
        controller.sitPassenger(passenger);
        controller.releasePassenger(passenger);

        passenger.setTransportationState(TransportationState.COMPLETED);
    }
}

