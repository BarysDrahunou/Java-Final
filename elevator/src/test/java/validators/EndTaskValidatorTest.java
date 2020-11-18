package validators;

import model.Elevator;
import model.Floor;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EndTaskValidatorTest {
    @Mock
    Logger LOGGER;
    List<Floor> floors = new ArrayList<>();
    List<Elevator> elevators = new ArrayList<>();
    int passengersNumber;
    Floor floor;
    Elevator elevator;

    @Before
    public void init() {
        passengersNumber = 5;
        floor = new Floor(5);
        floors.add(floor);
        elevator = new Elevator(4, 3, 3);
        elevators.add(elevator);
    }

    @Test
    public void validateResult() throws NoSuchFieldException, IllegalAccessException {
        Field field = EndTaskValidator.class.getDeclaredField("LOGGER");
        field.setAccessible(true);
        var lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
        VarHandle MODIFIERS = lookup.findVarHandle(Field.class, "modifiers", int.class);
        int mods = field.getModifiers();
        if (Modifier.isFinal(mods) && Modifier.isStatic(mods)) {
            MODIFIERS.set(field, mods & ~Modifier.FINAL);
        }
        field.set(EndTaskValidator.class, LOGGER);
        EndTaskValidator.validateResult(floors, elevators, passengersNumber);
        verify(LOGGER, times(5)).debug(anyString());
    }
}