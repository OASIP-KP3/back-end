package sit.int221.oasipservice.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.repo.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository repo;

    @Autowired
    public UniqueEmailValidator(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return repo.findByUserEmail(email) == null;
    }
}
