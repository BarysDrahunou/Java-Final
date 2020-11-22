package springcore.services;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class EmployeeCreatorTest {

    EmployeeCreator employeeCreator1;
    EmployeeCreator employeeCreator2;
    List<String> baseNames;
    List<String> baseSurnames;

    @Before
    public void init() {
        baseNames = new ArrayList<>(Arrays.asList("Michael",
                "Christopher",
                "Jessica"));
        baseSurnames = new ArrayList<>(Arrays.asList("Smith",
                "Johnson",
                "Williams"));

        employeeCreator1 = new EmployeeCreator("src/main/resources/testNames.txt",
                "src/main/resources/testSurnames.txt");
        employeeCreator2 = new EmployeeCreator("random",
                "random");
    }

    @Test
    public void createEmployeeAndGet() {
        List<String> names = new ArrayList<>();
        List<String> surnames = new ArrayList<>();

        IntStream
                .range(0, 500)
                .mapToObj(x -> employeeCreator1.createEmployeeAndGet())
                .forEach(employee -> {
                    names.add(employee.getName());
                    surnames.add(employee.getSurname());
                });

        assertTrue(baseNames.containsAll(names));
        assertTrue(baseSurnames.containsAll(surnames));
        assertTrue(IntStream
                .range(0, 500)
                .mapToObj(x -> employeeCreator2.createEmployeeAndGet())
                .allMatch(employee -> employee.getName().equals("John") && employee.getSurname().equals("Doe")));
    }
}