package sit.int221.oasipservice.dto.categories.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryNameDto {
    @NotNull
    @Min(1)
    private Integer id;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String categoryName;

    public CategoryNameDto setCategoryName(String categoryName) {
        this.categoryName = categoryName.trim();
        return this;
    }
}
