package sit.int221.oasipservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableException extends RuntimeException {
    public UnprocessableException() {
        this("Unprocessable!");
    }

    public UnprocessableException(String message) {
        this(message, (Throwable) null);
    }

    public UnprocessableException(String message, Throwable cause) {
        super(message, cause);
    }
}
