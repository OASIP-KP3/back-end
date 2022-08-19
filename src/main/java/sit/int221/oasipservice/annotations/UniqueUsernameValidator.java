package sit.int221.oasipservice.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.repo.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository repo;

    @Autowired
    public UniqueUsernameValidator(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return repo.findByUserName(username) == null;
    }
}
