package sit.int221.oasipservice.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}
