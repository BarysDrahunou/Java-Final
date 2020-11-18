package springcore.annotations;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

public class RandomAnnotationBeanPostProcessorTest {

    @Mock
    Logger LOGGER;
    RandomAnnotationBeanPostProcessor processor;
    List<String> randomNames;
    List<String> randomSurnames;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        randomNames = new ArrayList<>(Arrays.asList("Vitya", "Vasya", "Petya", "Igorek"));
        randomSurnames = new ArrayList<>(Arrays.asList("Petrov", "Sidorov"));
        processor = new RandomAnnotationBeanPostProcessor();
        processor = new RandomAnnotationBeanPostProcessor();
        Field names = RandomAnnotationBeanPostProcessor
                .class.getDeclaredField("names");
        names.setAccessible(true);
        ReflectionUtils.setField(names, processor, randomNames);
        Field surnames = RandomAnnotationBeanPostProcessor
                .class.getDeclaredField("surnames");
        surnames.setAccessible(true);
        ReflectionUtils.setField(surnames, processor, randomSurnames);
        Field field = RandomAnnotationBeanPostProcessor.class.getDeclaredField("LOGGER");
        field.setAccessible(true);
        var lookup = MethodHandles.privateLookupIn(Field.class,
                MethodHandles.lookup());
        VarHandle MODIFIERS = lookup.findVarHandle(Field.class, "modifiers", int.class);
        int mods = field.getModifiers();
        if (Modifier.isFinal(mods) && Modifier.isStatic(mods)) {
            MODIFIERS.set(field, mods & ~Modifier.FINAL);
        }
        field.set(RandomAnnotationBeanPostProcessor.class, LOGGER);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void setName() throws NoSuchFieldException, IllegalAccessException {
        processor.setNames("RandomPath");
        verify(LOGGER).error(any(IOException.class));
        Field field = processor.getClass().getDeclaredField("names");
        field.setAccessible(true);
        List<String> names = (List<String>) field.get(processor);
        assertEquals(1, names.size());
        assertEquals("John", names.get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void setSurname() throws NoSuchFieldException, IllegalAccessException {
        processor.setSurnames("RandomPath");
        verify(LOGGER).error(any(IOException.class));
        Field field = processor.getClass().getDeclaredField("surnames");
        field.setAccessible(true);
        List<String> surnames = (List<String>) field.get(processor);
        assertEquals(1, surnames.size());
        assertEquals("Doe", surnames.get(0));
    }

    @Test
    public void postProcessBeforeInitialization() {
        List<TestClass> testClassList = new ArrayList<>();
        for (int i = 0; i <= 500; i++) {
            TestClass bean = new TestClass();
            processor.postProcessBeforeInitialization(bean, "randomName");
            testClassList.add(bean);
        }
        assertTrue(testClassList
                .stream()
                .allMatch(bean -> randomNames.contains(bean.randomName)
                        && randomSurnames.contains(bean.randomSurname)));
        randomNames.clear();
        randomSurnames.clear();
        List<TestClass> emptyNamesSurnamesList = new ArrayList<>();
        processor.setNames("RandomPath");
        processor.setSurnames("RandomPath");
        for (int i = 0; i <= 500; i++) {
            TestClass bean = new TestClass();
            processor.postProcessBeforeInitialization(bean, "randomName");
            emptyNamesSurnamesList.add(bean);
        }
        assertTrue(emptyNamesSurnamesList
                .stream()
                .allMatch(bean -> bean.randomName.equals("John")
                        && bean.randomSurname.equals("Doe")));
    }
}