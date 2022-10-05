package sit.int221.oasipservice.annotations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.repositories.RoleRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class IsRoleValidator implements ConstraintValidator<IsRole, String> {
    private final RoleRepository repo;

    @Override
    public void initialize(IsRole constraintAnnotation) {
    }

    @Override
    public boolean isValid(String userRole, ConstraintValidatorContext context) {
        return repo.findByRoleName(userRole) != null;
    }
}
