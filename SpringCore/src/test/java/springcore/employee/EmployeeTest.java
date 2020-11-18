package springcore.employee;

import org.junit.Before;
import org.junit.Test;
import springcore.position.Position;
import springcore.statuses.EmployeeStatus;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class EmployeeTest {

    Employee employee1;
    Employee employee2;
    Employee employee3;

    @Before
    public void init() {
        employee1 = new Employee("Vitali", "Egorov");
        employee2 = new Employee();
        employee3 = new Employee("Sergey", "Drobotenko");
    }

    @Test
    public void setAndGetId() {
        assertEquals(0, employee1.getId());
        employee1.setId(11);
        assertEquals(11, employee1.getId());
        employee1.setId(111);
        assertEquals(111, employee1.getId());
    }

    @Test
    public void setAndGetStatus() {
        assertNull(employee2.getStatus());
        employee2.setStatus(EmployeeStatus.WORKS);
        assertEquals(EmployeeStatus.WORKS, employee2.getStatus());
        employee2.setStatus(EmployeeStatus.FIRED);
        assertEquals(EmployeeStatus.FIRED, employee2.getStatus());
    }

    @Test
    public void setAndGetPosition() {
        assertNull(employee3.getPosition());
        employee3.setPosition(new Position("Ment"));
        assertEquals(new Position("Ment"), employee3.getPosition());
        employee3.setPosition(new Position("Doctor"));
        assertEquals(new Position("Doctor"), employee3.getPosition());
        assertNotEquals(new Position("Ment"), employee3.getPosition());
    }

    @Test
    public void setAndGetPersonalBonuses() {
        assertEquals(BigDecimal.ZERO, employee1.getPersonalBonuses());
        employee1.setPersonalBonuses(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, employee1.getPersonalBonuses());
        employee1.setPersonalBonuses(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, employee1.getPersonalBonuses());
        assertNotEquals(BigDecimal.TEN, employee1.getPersonalBonuses());
    }

    @Test
    public void setAndGetTimeWorked() {
        assertEquals(0, employee2.getTimeWorked());
        employee2.setTimeWorked(5);
        assertEquals(5, employee2.getTimeWorked());
        employee2.setTimeWorked(8);
        assertEquals(8, employee2.getTimeWorked());
        assertNotEquals(5, employee2.getTimeWorked());
    }

    @Test
    public void getName() {
        assertEquals("Vitali", employee1.getName());
        assertNull(employee2.getName());
        assertEquals("Sergey", employee3.getName());
    }

    @Test
    public void getSurname() {
        assertEquals("Egorov", employee1.getSurname());
        assertNull(employee2.getName());
        assertEquals("Drobotenko", employee3.getSurname());
    }

    @Test
    public void testEquals() {
        assertNotEquals(employee1, employee2);
        assertNotEquals(employee2, employee3);
        assertNotEquals(employee3, employee1);
        assertEquals(employee2, new Employee());
        assertEquals(employee1, new Employee("Vitali", "Egorov"));
    }

    @Test
    public void testHashCode() {
        assertNotEquals(employee1.hashCode(), employee2.hashCode());
        assertNotEquals(employee2.hashCode(), employee3.hashCode());
        assertNotEquals(employee3.hashCode(), employee1.hashCode());
        assertEquals(employee2.hashCode(), new Employee().hashCode());
        assertEquals(employee1.hashCode(),
                new Employee("Vitali", "Egorov").hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("Vitali Egorov", employee1.toString());
        assertEquals("Sergey Drobotenko", employee3.toString());
        assertNotEquals(employee1.toString(), employee3.toString());
        assertNotEquals(employee1.toString(), employee2.toString());
        assertEquals("null null", employee2.toString());
        assertEquals(new Employee("Sergey", "Drobotenko").toString(),
                employee3.toString());
    }
}