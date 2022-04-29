package sit.int221.oasipservice.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Integer id;
    private String categoryName;
    private String categoryDescription;
    private Integer eventDuration;
}
