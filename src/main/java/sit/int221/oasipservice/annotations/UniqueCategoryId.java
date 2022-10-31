package sit.int221.oasipservice.annotations;

import sit.int221.oasipservice.annotations.validators.UniqueCategoryIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueCategoryIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCategoryId {
    String message() default "Category id is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
