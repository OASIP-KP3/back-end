package sit.int221.oasipservice.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FutureValidator implements ConstraintValidator<FutureValidation, LocalDateTime> {
    @Override
    public void initialize(FutureValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
        return dateTime.isAfter(LocalDateTime.now(ZoneId.systemDefault()));
    }
}
