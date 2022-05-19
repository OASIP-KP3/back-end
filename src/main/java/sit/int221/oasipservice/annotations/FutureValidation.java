package sit.int221.oasipservice.annotations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FutureValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureValidation {
    String message() default "must be a date and time in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
