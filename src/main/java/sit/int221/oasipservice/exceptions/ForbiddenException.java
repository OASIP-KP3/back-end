package sit.int221.oasipservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        this("Forbidden to access this resource!");
    }

    public ForbiddenException(String message) {
        this(message, (Throwable) null);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}