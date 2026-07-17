package com.smarttask.taskallocator.infrastructure.adapter.in.web;

import com.smarttask.taskallocator.domain.exception.CapacityExceededException;
import com.smarttask.taskallocator.domain.exception.DomainException;
import com.smarttask.taskallocator.domain.exception.NoAvailableMemberException;
import com.smarttask.taskallocator.infrastructure.adapter.in.web.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Traduce las excepciones a respuestas HTTP coherentes, evitando que detalles
 * técnicos se filtren al cliente.
 *
 * <ul>
 *   <li>Validación de campos / argumentos inválidos → 400</li>
 *   <li>Sin capacidad / miembro disponible → 409</li>
 *   <li>Resto de reglas de negocio → 422</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
        ApiError body = ApiError.validation(
                HttpStatus.BAD_REQUEST.value(), "Bad Request",
                "Hay campos inválidos en la petición", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({CapacityExceededException.class, NoAvailableMemberException.class})
    public ResponseEntity<ApiError> handleConflict(DomainException ex) {
        ApiError body = ApiError.of(
                HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError body = ApiError.of(
                HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(DomainException ex) {
        ApiError body = ApiError.of(
                HttpStatus.UNPROCESSABLE_ENTITY.value(), "Unprocessable Entity", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }
}
