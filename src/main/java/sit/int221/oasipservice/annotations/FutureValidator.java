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
        final String TIME_ZONE = "GMT+07:00";
        return dateTime.isAfter(LocalDateTime.now(ZoneId.of(TIME_ZONE)));
    }
}
