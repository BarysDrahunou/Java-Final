package springcore.annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;


import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class RandomAnnotationBeanPostProcessorTest {

    RandomAnnotationBeanPostProcessor processor;
    List<String> randomNames;
    List<String> randomSurnames;

    @Before
    public void init() throws NoSuchFieldException {
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