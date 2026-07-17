package com.smarttask.taskallocator.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

/**
 * Cuerpo estándar de una respuesta de error de la API.
 *
 * <p>{@code fieldErrors} solo aparece en errores de validación de campos.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        int status,
        String error,
        String message,
        Instant timestamp,
        Map<String, String> fieldErrors) {

    public static ApiError of(int status, String error, String message) {
        return new ApiError(status, error, message, Instant.now(), null);
    }

    public static ApiError validation(int status, String error, String message,
                                      Map<String, String> fieldErrors) {
        return new ApiError(status, error, message, Instant.now(), fieldErrors);
    }
}
