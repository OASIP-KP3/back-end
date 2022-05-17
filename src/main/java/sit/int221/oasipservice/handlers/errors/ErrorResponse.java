package sit.int221.oasipservice.handlers.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private Integer statusCode;
    private String message;
    private List<ErrorObject> errors = new ArrayList<>();

    public ErrorResponse(LocalDateTime timestamp, HttpStatus status, Integer statusCode, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorResponse(LocalDateTime timestamp, HttpStatus status, Integer statusCode, List<ErrorObject> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.statusCode = statusCode;
        this.errors = errors;
    }
}
