package sit.int221.oasipservice.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsRoleValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsRole {
    String message() default "This role is not supported";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
