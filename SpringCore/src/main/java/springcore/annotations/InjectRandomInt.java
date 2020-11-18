package springcore.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRandomInt {
    int max();
}
