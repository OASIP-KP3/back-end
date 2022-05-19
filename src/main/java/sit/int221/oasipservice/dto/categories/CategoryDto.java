package sit.int221.oasipservice.dto.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    @NotNull
    @Min(1)
    private Integer id;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String categoryName;

    @Size(max = 500)
    private String categoryDescription;

    @NotNull
    @Min(1)
    @Max(480)
    private Integer eventDuration;

    public CategoryDto setCategoryName(String categoryName) {
        this.categoryName = categoryName.trim();
        return this;
    }

    public CategoryDto setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription == null || categoryDescription.isBlank() ? null : categoryDescription.trim();
        return this;
    }
}
