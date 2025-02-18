package com.salesianostriana.dam.delight_nook.security.exceptionhandling;

import com.salesianostriana.dam.delight_nook.user.error.ActivationExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class JwtControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(AuthenticationException ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());

        return ErrorResponse.builder(ex, detail)
                .header("WWW-Authenticate", "Bearer")
                .build();
    }

    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleJwtException(JwtException ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());

        detail.setTitle("Invalid token");

        return detail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                ex.getMessage());

        detail.setTitle("No authorization");

        return detail;
    }

    @ExceptionHandler(ActivationExpiredException.class)
    public ProblemDetail handleActivationExpired(ActivationExpiredException ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                ex.getMessage());

        detail.setTitle("No puede activar el usuario");

        return detail;
    }
}
