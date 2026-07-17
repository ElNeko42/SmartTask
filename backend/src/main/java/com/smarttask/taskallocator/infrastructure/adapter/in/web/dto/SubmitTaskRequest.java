package com.smarttask.taskallocator.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Cuerpo de la petición para enviar una tarea que debe asignarse.
 */
public record SubmitTaskRequest(

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "La urgencia es obligatoria (LOW, MEDIUM, HIGH, CRITICAL)")
        String urgency,

        @NotNull(message = "El esfuerzo es obligatorio")
        @Positive(message = "El esfuerzo debe ser mayor que 0")
        Integer effort) {
}
