package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static constants.Constants.INITIAL_FLOOR;
import static org.junit.Assert.*;

public class BuildingTest {

    Building building;
    List<Floor> floorList;

    @Before
    public void setUp() {
        floorList = IntStream.range(INITIAL_FLOOR, 10 + INITIAL_FLOOR)
                .mapToObj(Floor::new)
                .collect(Collectors.toList());
        building = new Building(floorList);
    }

    @Test
    public void getFloors() {
        assertEquals(floorList, building.getFloors());
        assertNotEquals(new ArrayList<>(), building.getFloors());
    }

    @Test
    public void getBuildingHeight() {
        assertEquals(10, building.getBuildingHeight());
        assertNotEquals(11, building.getBuildingHeight());
    }
}