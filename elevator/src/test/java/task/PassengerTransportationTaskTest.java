package task;

import controller.Controller;
import model.Passenger;
import model.TransportationState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PassengerTransportationTaskTest {

    @Mock
    Passenger passenger;
    @Mock
    Controller controller;
    @InjectMocks
    PassengerTransportationTask passengerTransportationTask;

    @Test
    public void run() {
        doNothing().when(controller).sitPassenger(passenger);
        doNothing().when(controller).releasePassenger(passenger);
        when(passenger.getTransportationState()).thenReturn(TransportationState.IN_PROGRESS);

        passengerTransportationTask.run();

        verify(controller, times(1)).sitPassenger(passenger);
        verify(controller, times(1)).releasePassenger(passenger);

        assertEquals(TransportationState.IN_PROGRESS, passenger.getTransportationState());
        assertNotEquals(TransportationState.NOT_STARTED, passenger.getTransportationState());
    }
}