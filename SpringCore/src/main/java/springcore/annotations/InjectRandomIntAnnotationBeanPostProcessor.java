package springcore.annotations;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * The type Inject random int annotation bean post processor.
 * The class for using InjectRandomInt annotation
 */
@Component
public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {

    /**
     * @param bean     object which fields may be annotated as @InjectRandomInt
     * @param beanName name of the bean which are used inside the method
     * @return bean object
     * @throws BeansException if bean cannot be instantiated
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);

            if (annotation != null) {
                int max = annotation.max() > 0 ? annotation.max() : 1;
                Random random = new Random();
                int value = random.nextInt(max) + 1;
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, value);
            }
        }

        return bean;
    }
}
