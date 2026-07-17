package com.smarttask.taskallocator.domain.model.task;

/**
 * Value Object que representa el título de una tarea.
 *
 * <p>Garantiza que un título siempre es válido: no vacío y con una longitud
 * dentro de los límites del dominio. Al ser un {@code record} es inmutable y
 * su igualdad es por valor, como corresponde a un Value Object.
 */
public record TaskTitle(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 120;

    public TaskTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío");
        }
        value = value.trim();
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "El título debe tener entre %d y %d caracteres (recibidos %d)"
                            .formatted(MIN_LENGTH, MAX_LENGTH, value.length()));
        }
    }

    /** Fábrica semántica: {@code TaskTitle.of("...")} se lee mejor que {@code new}. */
    public static TaskTitle of(String value) {
        return new TaskTitle(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
