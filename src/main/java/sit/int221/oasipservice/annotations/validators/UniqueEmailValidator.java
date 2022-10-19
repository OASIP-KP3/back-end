package sit.int221.oasipservice.annotations.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.annotations.UniqueEmail;
import sit.int221.oasipservice.repositories.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository repo;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return repo.findByUserEmail(email) == null;
    }
}
