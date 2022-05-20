package sit.int221.oasipservice.dto.categories.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDurationDto {
    @NotNull
    @Min(1)
    private Integer id;

    @NotNull
    @Min(1)
    @Max(480)
    private Integer eventDuration;
}
