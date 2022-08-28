package sit.int221.oasipservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        this("Not authorized!");
    }

    public UnauthorizedException(String message) {
        this(message, (Throwable) null);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
