package sit.int221.oasipservice.annotations;

import sit.int221.oasipservice.annotations.validators.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
