package sit.int221.oasipservice.annotations;

import sit.int221.oasipservice.annotations.validators.UniqueCategoryValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueCategoryValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCategory {
    String message() default "Category is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
