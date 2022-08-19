package sit.int221.oasipservice.dto.users;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListPageDto {
    private List<UserDto> content;
    private int number;
    private int size;
    private int totalPages;
    private int numberOfElements;
    private int totalElements;
    private boolean last;
    private boolean first;
}
