package com.smarttask.taskallocator.domain.model.task;

import java.util.Objects;
import java.util.UUID;

/**
 * Identidad de una {@link Task}.
 *
 * <p>Envolver el {@link UUID} en un Value Object evita pasar identificadores
 * "desnudos" por el dominio y hace imposible confundir el id de una tarea con
 * el de un miembro.
 */
public record TaskId(UUID value) {

    public TaskId {
        Objects.requireNonNull(value, "El id de la tarea no puede ser null");
    }

    public static TaskId generate() {
        return new TaskId(UUID.randomUUID());
    }

    public static TaskId of(UUID value) {
        return new TaskId(value);
    }

    public static TaskId of(String value) {
        return new TaskId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
