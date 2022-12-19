package sit.int221.oasipservice.handlers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sit.int221.oasipservice.exceptions.ForbiddenException;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.exceptions.UnprocessableException;
import sit.int221.oasipservice.handlers.errors.ErrorObject;
import sit.int221.oasipservice.handlers.errors.ErrorResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    private final String TIME_ZONE = "GMT+07:00";

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleInvalidArgument(MethodArgumentNotValidException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        List<ErrorObject> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorObject(fieldName, errorMessage));
        });
        return new ErrorResponse(now, BAD_REQUEST, BAD_REQUEST.value(), errors);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, NOT_FOUND, NOT_FOUND.value(), ex.getMessage());
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnprocessableException.class)
    public ErrorResponse handleUnprocessableException(UnprocessableException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, UNPROCESSABLE_ENTITY, UNPROCESSABLE_ENTITY.value(), ex.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, UNAUTHORIZED, UNAUTHORIZED.value(), ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponse handleForbiddenException(ForbiddenException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, FORBIDDEN, FORBIDDEN.value(), ex.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResponse handleTokenExpiredException(TokenExpiredException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, UNAUTHORIZED, UNAUTHORIZED.value(), ex.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(JWTDecodeException.class)
    public ErrorResponse handleJWTDecodeException(JWTDecodeException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, UNAUTHORIZED, UNAUTHORIZED.value(), ex.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(SignatureVerificationException.class)
    public ErrorResponse handleSignatureVerificationException(SignatureVerificationException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, UNAUTHORIZED, UNAUTHORIZED.value(), ex.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(ServletRequestBindingException.class)
    public ErrorResponse handleServletRequestBindingException(ServletRequestBindingException ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, UNAUTHORIZED, UNAUTHORIZED.value(), ex.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGlobalException(Exception ex) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        return new ErrorResponse(now, INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
