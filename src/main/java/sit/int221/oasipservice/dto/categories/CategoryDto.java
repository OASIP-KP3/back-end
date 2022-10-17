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

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[^\s].+[^\s]$", message = "leading or trailing white spaces do not allowed in the string")
    private String categoryName;

    @Size(max = 500)
    private String categoryDescription;

    @NotNull
    @Min(1)
    @Max(480)
    private Integer eventDuration;

    public CategoryDto setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription == null || categoryDescription.isBlank() ? null : categoryDescription.trim();
        return this;
    }
}
