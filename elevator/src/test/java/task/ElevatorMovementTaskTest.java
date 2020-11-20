package task;

import controller.Controller;
import model.Elevator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorMovementTaskTest {

    @Mock
    Elevator elevator;
    @Mock
    Controller controller;
    @Mock
    CountDownLatch countDownLatch;
    @Mock
    Lock lock;
    @InjectMocks
    ElevatorMovementTask elevatorMovementTask;

    @Test
    public void run() {
        when(controller.doTask(elevator)).thenReturn(true).thenReturn(true).thenReturn(false);
        doNothing().when(lock).lock();
        doNothing().when(lock).unlock();

        elevatorMovementTask.run();

        verify(lock, times(2)).lock();
        verify(lock, times(2)).unlock();
        verify(controller, times(2)).sitPassengers(elevator);
        verify(controller, times(2)).move(elevator);
        verify(controller, times(2)).releasePassengers(elevator);
        verify(countDownLatch, times(1)).countDown();
    }
}