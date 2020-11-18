package springcore.annotations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;

import static springcore.constants.VariablesConstants.*;

@Component
public class RandomAnnotationBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LogManager.getLogger();
    private List<String> names;
    private List<String> surnames;

    public RandomAnnotationBeanPostProcessor() {
    }

    @Autowired
    public void setNames(@Value(NAMES_PATH) String pathName) {
        try {
            this.names = Files.readAllLines(Paths.get(pathName));
        } catch (IOException e) {
            LOGGER.error(e);
            this.names = new ArrayList<>(Collections.singletonList(DEFAULT_NAME));
        }
    }

    @Autowired
    public void setSurnames(@Value(SURNAMES_PATH) String surnamesPath) {
        try {
            this.surnames = Files.readAllLines(Paths.get(surnamesPath));
        } catch (IOException e) {
            LOGGER.error(e);
            this.surnames = new ArrayList<>(Collections.singletonList(DEFAULT_SURNAME));
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            RandomName randomName = field.getAnnotation(RandomName.class);
            RandomSurname randomSurname = field.getAnnotation(RandomSurname.class);
            setRandomValue(bean, field, randomName, names);
            setRandomValue(bean, field, randomSurname, surnames);
        }
        return bean;
    }

    public static void setRandomValue(Object bean, Field field,
                                      Annotation annotation, List<String> values) {
        if (annotation != null) {
            String value = values.get(new Random().nextInt(values.size()));
            field.setAccessible(true);
            ReflectionUtils.setField(field, bean, value);
        }
    }
}
