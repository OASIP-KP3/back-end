package sit.int221.oasipservice.dto.categories.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDescDto {
    @NotNull
    @Min(1)
    private Integer id;

    @Size(max = 500)
    private String categoryDescription;

    public CategoryDescDto setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription == null || categoryDescription.isBlank() ? null : categoryDescription.trim();
        return this;
    }
}
