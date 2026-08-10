package com.example.demo.exception;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
@RestControllerAdvice public class GlobalExceptionHandler  {
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> validation(MethodArgumentNotValidException e,HttpServletRequest r) {
        Map<String,String> f=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x->f.putIfAbsent(x.getField(),x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ApiError(Instant.now(),400,"Bad Request","Hay datos inválidos",r.getRequestURI(),f));
    }
    @ExceptionHandler( {
        ConstraintViolationException.class,HttpMessageNotReadableException.class
    }
    ) ResponseEntity<ApiError> bad(Exception e,HttpServletRequest r) {
        return err(HttpStatus.BAD_REQUEST,"Hay datos inválidos",r);
    }
    @ExceptionHandler(ConflictException.class) ResponseEntity<ApiError> conflict(ConflictException e,HttpServletRequest r) {
        return err(HttpStatus.CONFLICT,e.getMessage(),r);
    }
    @ExceptionHandler(BusinessRuleException.class) ResponseEntity<ApiError> rule(BusinessRuleException e,HttpServletRequest r) {
        return err(HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage(),r);
    }
    @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ApiError> nf(ResourceNotFoundException e,HttpServletRequest r) {
        return err(HttpStatus.NOT_FOUND,e.getMessage(),r);
    }
    @ExceptionHandler(ServiceUnavailableException.class) ResponseEntity<ApiError> unavailable(ServiceUnavailableException e,HttpServletRequest r) {
        return err(HttpStatus.SERVICE_UNAVAILABLE,e.getMessage(),r);
    }
    @ExceptionHandler(InternalAccessDeniedException.class) ResponseEntity<ApiError> internal(InternalAccessDeniedException e,HttpServletRequest r) {
        return err(HttpStatus.FORBIDDEN,e.getMessage(),r);
    }
    @ExceptionHandler(BadCredentialsException.class) ResponseEntity<ApiError> unauth(BadCredentialsException e,HttpServletRequest r) {
        return err(HttpStatus.UNAUTHORIZED,"Email o contraseña incorrectos",r);
    }
    @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<ApiError> integrity(DataIntegrityViolationException e,HttpServletRequest r) {
        return err(HttpStatus.CONFLICT,"La operación entra en conflicto con datos existentes",r);
    }
    @ExceptionHandler(Exception.class) ResponseEntity<ApiError> any(Exception e,HttpServletRequest r) {
        return err(HttpStatus.INTERNAL_SERVER_ERROR,"Ocurrió un error inesperado",r);
    }
    private ResponseEntity<ApiError> err(HttpStatus s,String m,HttpServletRequest r) {
        return ResponseEntity.status(s).body(ApiError.of(s.value(),s.getReasonPhrase(),m,r.getRequestURI()));
    }
}
