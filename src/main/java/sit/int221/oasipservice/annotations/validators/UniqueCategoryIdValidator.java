package sit.int221.oasipservice.annotations.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.annotations.UniqueCategory;
import sit.int221.oasipservice.repositories.CategoryRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueCategoryIdValidator implements ConstraintValidator<UniqueCategory, Integer> {
    private final CategoryRepository repo;

    @Override
    public void initialize(UniqueCategory constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        return !repo.existsById(id);
    }
}
