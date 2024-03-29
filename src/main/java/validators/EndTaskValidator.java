package validators;

import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static constants.Constants.*;
import static constants.Messages.*;


public final class EndTaskValidator {

    private static final Logger LOGGER = LogManager.getLogger();

    private EndTaskValidator() {
    }

    public static void validateResult(List<Floor> floors, List<Elevator> elevators, int passengersNumber) {
        isAllDispatchContainersEmpty(floors);
        isElevatorsContainersEmpty(elevators);
        isAllStatusesCompleted(floors);
        isDestinationCorrect(floors);
        areAllPeopleWereTransported(floors, passengersNumber);
    }

    private static void isAllDispatchContainersEmpty(List<Floor> floors) {
        boolean isAllContainersEmpty = floors
                .stream()
                .map(floor -> floor.getDispatchContainer().size())
                .noneMatch(size -> size > 0);

        LOGGER.debug(String.format(IS_ALL_DISPATCH_CONTAINERS_ARE_EMPTY, isAllContainersEmpty));
    }

    private static void isElevatorsContainersEmpty(List<Elevator> elevators) {
        boolean isElevatorContainerEmpty = elevators
                .stream()
                .allMatch(elevator -> elevator.getContainer().size() == 0);

        LOGGER.debug(String.format(IS_ELEVATOR_CONTAINER_EMPTY, isElevatorContainerEmpty));
    }

    private static void isAllStatusesCompleted(List<Floor> floors) {
        boolean isAllStatusesIsCompleted = floors
                .stream()
                .map(Floor::getArrivalContainer)
                .allMatch(floor -> floor
                        .stream()
                        .map(Passenger::getTransportationState)
                        .allMatch(state -> state == TransportationState.COMPLETED));

        LOGGER.debug(String.format(IS_ALL_STATUSES_COMPLETED, isAllStatusesIsCompleted));
    }

    private static void isDestinationCorrect(List<Floor> floors) {
        boolean isDestinationCorrect = floors
                .stream()
                .allMatch(floor -> floor
                        .getDispatchContainer()
                        .stream()
                        .allMatch(passenger -> passenger.getDestinationFloor() == floor.getFloorNumber()));

        LOGGER.debug(String.format(IS_DESTINATION_CORRECT, isDestinationCorrect));
    }

    private static void areAllPeopleWereTransported(List<Floor> floors, int passengersNumber) {
        boolean areAllPeopleWereTransported = floors
                .stream()
                .map(floor -> floor.getArrivalContainer().size())
                .reduce(INITIAL_SUM, Integer::sum) == passengersNumber;

        LOGGER.debug(String.format(ARE_ALL_PEOPLE_WERE_TRANSPORTED, areAllPeopleWereTransported));
    }
}