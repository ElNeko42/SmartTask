package com.smarttask.taskallocator.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Cuerpo de la petición para dar de alta a un miembro.
 */
public record RegisterMemberRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotNull(message = "La capacidad es obligatoria")
        @Positive(message = "La capacidad debe ser mayor que 0")
        Integer maxCapacity) {
}
