package sit.int221.oasipservice.handlers;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.exceptions.UnprocessableException;
import sit.int221.oasipservice.handlers.errors.ErrorObject;
import sit.int221.oasipservice.handlers.errors.ErrorResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    private final String TIME_ZONE = "GMT+07:00";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleInvalidArgument(MethodArgumentNotValidException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        List<ErrorObject> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorObject(fieldName, errorMessage));
        });
        return new ErrorResponse(now, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse handleServerWebInputException(ResponseStatusException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, ex.getStatus(), ex.getRawStatusCode(), ex.getReason());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnprocessableException.class)
    public ErrorResponse handleServerWebInputException(UnprocessableException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, HttpStatus.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGlobalException(Exception ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
