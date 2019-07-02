package fr.rekeningrijders.rest.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TrackerValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackerValid {
    String message() default "{error.trackerId}";
    boolean allowNull() default false;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
