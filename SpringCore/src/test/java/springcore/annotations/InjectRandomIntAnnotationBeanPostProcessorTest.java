package springcore.annotations;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class InjectRandomIntAnnotationBeanPostProcessorTest {

    InjectRandomIntAnnotationBeanPostProcessor processor;


    @Before
    public void init() {
        processor = new InjectRandomIntAnnotationBeanPostProcessor();
    }

    @Test
    public void postProcessBeforeInitialization() {
        List<TestClass> beans = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            TestClass bean = (TestClass) processor
                    .postProcessBeforeInitialization(new TestClass(), "BeanName");
            beans.add(bean);
        }
        assertTrue(beans
                .stream()
                .allMatch(bean1 -> bean1.notRandomField == 15
                        && bean1.randomField > 0
                        && bean1.randomField <= 20));
    }
}