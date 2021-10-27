package springcore.annotations;

import java.lang.annotation.*;

/**
 * The interface Inject random int.
 * Inserts a random int value into an annotated field
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRandomInt {
    /**
     * Max int.
     *
     * @return the upper possible bound of a field
     */
    int max();
}
