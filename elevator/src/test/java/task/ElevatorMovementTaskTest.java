package task;

import controller.Controller;
import model.Elevator;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorMovementTaskTest {
    @Mock
    Logger LOGGER;
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
    public void run() throws NoSuchFieldException, IllegalAccessException {
        Field field = ElevatorMovementTask.class.getDeclaredField("LOGGER");
        field.setAccessible(true);
        var lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
        VarHandle MODIFIERS = lookup.findVarHandle(Field.class, "modifiers", int.class);
        int mods = field.getModifiers();
        if (Modifier.isFinal(mods)) {
            MODIFIERS.set(field, mods & ~Modifier.FINAL);
        }
        field.set(elevatorMovementTask, LOGGER);
        when(controller.doTask(elevator)).thenReturn(true).thenReturn(true).thenReturn(false);
        doNothing().when(lock).lock();
        doNothing().when(lock).unlock();
        elevatorMovementTask.run();
        verify(LOGGER, times(2)).info(anyString());
        verify(lock, times(2)).lock();
        verify(lock, times(2)).unlock();
        verify(controller, times(2)).sitPassengers(elevator);
        verify(controller, times(2)).move(elevator);
        verify(controller, times(2)).releasePassengers(elevator);
        verify(countDownLatch, times(1)).countDown();
    }
}