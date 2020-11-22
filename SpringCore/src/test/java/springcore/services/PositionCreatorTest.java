package springcore.services;

import org.junit.*;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class PositionCreatorTest {

    PositionCreator positionCreator1;
    PositionCreator positionCreator2;
    List<String> baseJobs;

    @Before
    public void setUp() {
        baseJobs = new ArrayList<>(Arrays.asList("Auditor",
                "Beggar",
                "Accountant"));

        positionCreator1 = new PositionCreator("src/main/resources/testJobs.txt");
        positionCreator2 = new PositionCreator("random");
    }

    @Test
    public void createPositionAndGet() {
        List<String> jobs = new ArrayList<>();

        assertTrue(IntStream
                .range(0, 500)
                .mapToObj(x -> positionCreator1.createPositionAndGet())
                .peek(position -> jobs.add(position.getPositionName()))
                .allMatch(position -> position.getVacancies() == 1
                        && position.getActiveWorkers() == 0
                        && position.getSalary().getValue() == 0));
        assertTrue(baseJobs.containsAll(jobs));

        assertTrue(IntStream
                .range(0, 500)
                .mapToObj(x -> positionCreator2.createPositionAndGet())
                .allMatch(position -> position.getPositionName().equals("Actor")));
    }
}