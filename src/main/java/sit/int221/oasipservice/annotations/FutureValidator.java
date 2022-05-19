package sit.int221.oasipservice.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureValidator implements ConstraintValidator<FutureValidation, LocalDateTime> {
    @Override
    public void initialize(FutureValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
        return dateTime.isAfter(LocalDateTime.now());
    }
}
