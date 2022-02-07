package com.fsoft.quizsystem.config.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fsoft.quizsystem.object.dto.response.ErrorResponse;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class RestfulExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception);
        ErrorResponse response = ErrorResponse.builder()
                                              .responseCode(HttpStatus.BAD_REQUEST.value())
                                              .message(exception.getMessage())
                                              .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            BindException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult()
                 .getAllErrors()
                 .forEach((error) -> {
                     String fieldName = ((FieldError) error).getField();
                     String errorMessage = error.getDefaultMessage();
                     errors.put(fieldName, errorMessage);
                 });
        String message = "Something were wrong with your query, please check errors and try again.";

        ErrorResponse response = ErrorResponse.builder()
                                              .responseCode(HttpStatus.BAD_REQUEST.value())
                                              .message(message)
                                              .errors(errors)
                                              .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintException(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put(exception.getConstraintName(), exception.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                                              .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                              .message(exception.getMessage())
                                              .errors(errors)
                                              .build();


        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            AccessDeniedException exception) {
        ErrorResponse response = ErrorResponse.builder().build();

        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            log.error(SecurityContextHolder.getContext().getAuthentication());
            response.setResponseCode(HttpStatus.FORBIDDEN.value());
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(
            TokenExpiredException exception) {
        ErrorResponse response = ErrorResponse.builder()
                                              .responseCode(HttpStatus.UNAUTHORIZED.value())
                                              .message(exception.getMessage())
                                              .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            ResourceNotFoundException exception) {
        String message = ObjectUtils.isEmpty(exception.getMessage())
                         ? "Resource not found"
                         : exception.getMessage();

        ErrorResponse response = ErrorResponse.builder()
                                              .responseCode(HttpStatus.NOT_FOUND.value())
                                              .message(message)
                                              .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException exception) {
        ErrorResponse response = ErrorResponse.builder()
                                              .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                              .message(exception.getMessage())
                                              .build();

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> handleServerException(ServletException exception) {
        ErrorResponse response = ErrorResponse.builder()
                                              .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                              .message(exception.getMessage())
                                              .build();

        return ResponseEntity.internalServerError().body(response);
    }

}
