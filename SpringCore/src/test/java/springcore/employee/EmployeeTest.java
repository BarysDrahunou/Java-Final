package springcore.employee;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class EmployeeTest {

    Employee employee1;
    Employee employee2;
    Employee employee3;

    @Before
    public void init() {
        employee1 = new Employee("Vitali", "Egorov");
        employee2 = new Employee("Igor","Suvorov");
        employee3 = new Employee("Sergey", "Drobotenko");
    }

    @Test
    public void testEquals() {
        assertNotEquals(employee1, employee2);
        assertNotEquals(employee2, employee3);
        assertNotEquals(employee3, employee1);
        assertEquals(employee2, new Employee("Igor","Suvorov"));
        assertEquals(employee1, new Employee("Vitali", "Egorov"));
    }

    @Test
    public void testHashCode() {
        assertNotEquals(employee1.hashCode(), employee2.hashCode());
        assertNotEquals(employee2.hashCode(), employee3.hashCode());
        assertNotEquals(employee3.hashCode(), employee1.hashCode());
        assertEquals(employee2.hashCode(), new Employee("Igor","Suvorov").hashCode());
        assertEquals(employee1.hashCode(),
                new Employee("Vitali", "Egorov").hashCode());
    }
}