package sit.int221.oasipservice.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.repositories.RoleRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class IsRoleValidator implements ConstraintValidator<IsRole, String> {

    private final RoleRepository repo;

    @Autowired
    public IsRoleValidator(RoleRepository repo) {
        this.repo = repo;
    }

    @Override
    public void initialize(IsRole constraintAnnotation) {
    }

    @Override
    public boolean isValid(String userRole, ConstraintValidatorContext context) {
        return repo.findByRoleName(userRole) != null;
    }
}
