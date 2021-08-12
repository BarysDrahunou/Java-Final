package controller;

import model.*;
import org.junit.*;
import org.mockito.*;

import java.util.*;

import static model.MovementDirection.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ControllerTest {

    @Mock
    Elevator elevator;
    Building building;
    List<Elevator> elevators;
    List<Floor> floors;
    Controller controller;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        elevators = new ArrayList<>();
        floors = new ArrayList<>(Arrays.asList(new Floor(1), new Floor(2),
                new Floor(3),new Floor(4),new Floor(5)));
        building = new Building(floors);
        controller = new Controller(building, elevators, 100500);
    }

    @Test
    @SuppressWarnings("all")
    public void move() {
        when(elevator.getCurrentFloorNumber()).thenReturn(5,2,4);
        when(elevator.getMovementDirection()).thenReturn(DOWN, DOWN,UP);

        controller.move(elevator);
        controller.move(elevator);
        controller.move(elevator);

        verify(elevator,times(3)).getCurrentFloorNumber();
        verify(elevator,times(3)).getMovementDirection();

        verify(elevator).setCurrentFloorNumber(eq(4));
        verify(elevator).setCurrentFloorNumber(eq(1));
        verify(elevator).setCurrentFloorNumber(eq(5));
        verify(elevator).setMovementDirection(UP);
        verify(elevator).setMovementDirection(DOWN);
    }
}