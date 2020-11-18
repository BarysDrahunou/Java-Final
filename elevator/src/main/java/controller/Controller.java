package controller;

import model.*;
import org.apache.logging.log4j.*;

import java.util.*;
import java.util.concurrent.locks.*;
import java.util.stream.*;

import static constants.Constants.*;


public class Controller {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Building building;
    private final List<Elevator> elevators;
    private final Map<Passenger, Elevator> passengerElevatorMap = new HashMap<>();
    private final Lock lock;
    private final List<Condition> floorPassengersConditionsList;
    private final List<Condition> elevatorPassengersConditionsList;
    private final Condition elevatorCondition;
    private int passengerCount;
    private Elevator activeElevator;

    public Controller(Building building, List<Elevator> elevators, int passengerCount) {
        this.building = building;
        this.elevators = new ArrayList<>(elevators);
        this.lock = new ReentrantLock();
        this.floorPassengersConditionsList = getConditions();
        this.elevatorPassengersConditionsList = getConditions();
        this.elevatorCondition = lock.newCondition();
        this.passengerCount = passengerCount;
    }

    private List<Condition> getConditions() {
        return IntStream
                .range(0, building.getBuildingHeight())
                .mapToObj(x -> lock.newCondition())
                .collect(Collectors.toList());
    }

    public void sitPassenger(Passenger passenger) {
        lock.lock();
        try {
            while (!canElevatorAcceptAPassenger(activeElevator, passenger)) {
                floorPassengersConditionsList.get(passenger.getSourceFloor() - 1).await();
            }
            Floor currentFloor = getCurrentFloor(activeElevator);
            currentFloor.getDispatchContainer().remove(passenger);
            passengerCount--;
            List<Passenger> elevatorContainer = activeElevator.getContainer();
            elevatorContainer.add(passenger);
            passengerElevatorMap.put(passenger, activeElevator);
            LOGGER.info(String.format(BOARDING_OF_PASSENGER, passenger.getPersonalId(),
                    currentFloor.getFloorNumber(), activeElevator.getPersonalId()));
            elevatorCondition.signal();
        } catch (InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    private boolean canElevatorAcceptAPassenger(Elevator elevator, Passenger passenger) {
        return elevator != null
                && elevator.hasFreeSpace()
                && passenger.getMovementDirection() == elevator.getMovementDirection()
                && elevator.getCurrentFloorNumber()==passenger.getSourceFloor();
    }

    public void releasePassenger(Passenger passenger) {
        lock.lock();
        try {
            Elevator elevator = passengerElevatorMap.get(passenger);
            while (elevator.getCurrentFloorNumber() != passenger.getDestinationFloor()) {
                elevatorPassengersConditionsList.get(passenger.getDestinationFloor() - 1).await();
            }
            Floor currentFloor = getCurrentFloor(elevator);
            currentFloor.addPassengerToArrivalContainer(passenger);
            List<Passenger> elevatorContainer = elevator.getContainer();
            elevatorContainer.remove(passenger);
            LOGGER.info(String.format(DEBOARDING_OF_PASSENGER, passenger.getPersonalId(),
                    currentFloor.getFloorNumber(), elevator.getPersonalId()));
            elevatorCondition.signal();
        } catch (InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void sitPassengers(Elevator elevator) {
        lock.lock();
        try {
            activeElevator = elevator;
            floorPassengersConditionsList.get(elevator.getCurrentFloorNumber() - 1).signalAll();
            while (elevator.hasFreeSpace() && getCurrentFloor(elevator)
                    .getDispatchContainer()
                    .stream()
                    .anyMatch(passenger -> passenger.getMovementDirection()
                            == elevator.getMovementDirection())) {
                elevatorCondition.await();
            }
            activeElevator = null;
        } catch (InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    private Floor getCurrentFloor(Elevator elevator) {
        return building.getFloors().get(elevator.getCurrentFloorNumber() - 1);
    }

    public void move(Elevator elevator) {
        int currentFloor = elevator.getCurrentFloorNumber();
        int old_floor = currentFloor;
        MovementDirection currentDirection = elevator.getMovementDirection();
        if (currentDirection == MovementDirection.UP) {
            elevator.setCurrentFloorNumber(++currentFloor);
        } else {
            elevator.setCurrentFloorNumber(--currentFloor);
        }
        LOGGER.info(String.format(MOVING_ELEVATOR, elevator.getPersonalId(),
                old_floor, currentFloor));
        if (currentFloor == INITIAL_FLOOR) {
            elevator.setMovementDirection(MovementDirection.UP);
        }
        if (currentFloor == building.getBuildingHeight()) {
            elevator.setMovementDirection(MovementDirection.DOWN);
        }
    }

    public void releasePassengers(Elevator elevator) {
        lock.lock();
        try {
            elevatorPassengersConditionsList.get(elevator.getCurrentFloorNumber() - 1)
                    .signalAll();
            while (elevator
                    .getContainer()
                    .stream()
                    .anyMatch(passenger -> passenger.getDestinationFloor()
                            == elevator.getCurrentFloorNumber())) {
                elevatorCondition.await();
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public boolean doTask(Elevator elevator) {
        lock.lock();
        try {
            if (elevator.getContainer().size() > 0
                    || passengerCount >
                    getActiveElevatorsFreeCapacity() - elevator.getCapacity()) {
                return true;
            } else {
                elevators.remove(elevator);
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    private int getActiveElevatorsFreeCapacity() {
        return elevators.stream()
                .map(elevator -> elevator.getCapacity() - elevator.getContainer().size())
                .reduce(0, Integer::sum);
    }
}