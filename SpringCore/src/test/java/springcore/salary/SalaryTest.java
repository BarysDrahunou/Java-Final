package springcore.salary;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import springcore.currency.Usd;
import springcore.employee.Employee;


import java.math.BigDecimal;

import static org.junit.Assert.*;

public class SalaryTest {

    Salary salary1;
    Salary salary2;
    Salary salary3;
    Salary salary4;
    Salary salary5;
    Salary salary6;
    Employee employee;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        salary1 = new Salary(new Usd(700), 3, BigDecimal.ONE);
        salary2 = new Salary(new Usd(300), 5, BigDecimal.TEN.negate());
        salary3 = new Salary(new Usd(500), 0, BigDecimal.ZERO);
        salary4 = new Salary(new Usd(700), 0, BigDecimal.ONE);
        salary5 = new Salary(new Usd(700), 0, BigDecimal.ONE.negate());
        salary6 = new Salary(new Usd(700), 5, BigDecimal.ZERO);

        employee = new Employee("Vitali", "Burakovski");
    }

    @Test
    public void changeSalaryFromInflation() {
        assertEquals(new Usd(11),
                Salary.changeSalaryFromInflation(10, new Usd(10)));
        assertEquals(new Usd(366),
                Salary.changeSalaryFromInflation(10, new Usd(333)));
        assertEquals(new Usd(720),
                Salary.changeSalaryFromInflation(20, new Usd(600)));
        assertEquals(new Usd(11),
                Salary.changeSalaryFromInflation(0, new Usd(11)));

        assertNotEquals(new Usd(11),
                Salary.changeSalaryFromInflation(10, new Usd(11)));
    }

    @Test
    public void getSalaryWithBonuses() {
        assertEquals(910,salary1
                .getSalaryWithBonuses(new Usd(10),new Usd(200)).getValue());
        assertEquals(500,salary2
                .getSalaryWithBonuses(new Usd(0),new Usd(200)).getValue());
        assertEquals(510,salary3
                .getSalaryWithBonuses(new Usd(10),new Usd(0)).getValue());
    }
}