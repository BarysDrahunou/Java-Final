package springcore.salary;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import springcore.currency.Usd;
import springcore.employee.Employee;


import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SalaryTest {

    @Mock
    Logger LOGGER;
    Salary salary1;
    Salary salary2;
    Salary salary3;
    Salary salary4;
    Salary salary5;
    Salary salary6;
    Employee employee;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        salary1 = new Salary(new Usd(700), 3, BigDecimal.ONE);
        salary2 = new Salary(new Usd(300), 5, BigDecimal.TEN.negate());
        salary3 = new Salary(new Usd(500), 0, BigDecimal.ZERO);
        salary4 = new Salary(new Usd(700), 0, BigDecimal.ONE);
        salary5 = new Salary(new Usd(700), 0, BigDecimal.ONE.negate());
        salary6 = new Salary(new Usd(700), 5, BigDecimal.ZERO);
        employee = new Employee("Vitali", "Burakovski");
        Field field = Salary.class.getDeclaredField("LOGGER");
        field.setAccessible(true);
        var lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
        VarHandle MODIFIERS = lookup.findVarHandle(Field.class, "modifiers", int.class);
        int mods = field.getModifiers();
        if (Modifier.isFinal(mods) && Modifier.isStatic(mods)) {
            MODIFIERS.set(field, mods & ~Modifier.FINAL);
        }
        field.set(Salary.class, LOGGER);
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
    public void paySalary() {
        salary1.paySalary(employee);
        salary2.paySalary(employee);
        salary3.paySalary(employee);
        salary4.paySalary(employee);
        salary5.paySalary(employee);
        salary6.paySalary(employee);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(LOGGER,times(9)).info(argumentCaptor.capture());
        List<String> logMessages=argumentCaptor.getAllValues();
        assertEquals("Employee Vitali Burakovski will receive an experience" +
                " bonus in amount 0.70", logMessages.get(0));
        assertEquals("Employee Vitali Burakovski received a salary 7.00 with " +
                "bonus 0.07 and experience bonus 0.70, in total 7.77", logMessages.get(1));
        assertEquals("Employee Vitali Burakovski will receive an experience" +
                " bonus in amount 0.30", logMessages.get(2));
        assertEquals("Employee Vitali Burakovski received a salary 3.00 with " +
                "fine -0.30 and experience bonus 0.30, in total 3.00", logMessages.get(3));
        assertEquals("Employee Vitali Burakovski received a salary 5.00",
                logMessages.get(4));
        assertEquals("Employee Vitali Burakovski received a salary 7.00 " +
                "with bonus 0.07, in total 7.07", logMessages.get(5));
        assertEquals("Employee Vitali Burakovski received a salary 7.00 with fine -0.07," +
                        " in total 6.93", logMessages.get(6));
        assertEquals("Employee Vitali Burakovski will receive an experience bonus" +
                " in amount 0.70", logMessages.get(7));
        assertEquals("Employee Vitali Burakovski received a salary 7.00 with " +
                "experience bonus 0.70, in total 7.70", logMessages.get(8));
    }
}