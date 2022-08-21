package sit.int221.oasipservice.annotations;

import sit.int221.oasipservice.enumtype.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsRoleValidator implements ConstraintValidator<IsRole, String> {

    @Override
    public void initialize(IsRole constraintAnnotation) {
    }

    @Override
    public boolean isValid(String userRole, ConstraintValidatorContext context) {
        String temp = userRole.trim().toLowerCase();
        for (Role role : Role.values()) {
            if (temp.equals(role.getRole())) {
                return true;
            }
        }
        return false;
    }
}
