package sit.int221.oasipservice.annotations.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.annotations.UniqueCategoryId;
import sit.int221.oasipservice.repositories.CategoryRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueCategoryIdValidator implements ConstraintValidator<UniqueCategoryId, Integer> {
    private final CategoryRepository repo;

    @Override
    public void initialize(UniqueCategoryId constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        return !repo.existsById(id);
    }
}
