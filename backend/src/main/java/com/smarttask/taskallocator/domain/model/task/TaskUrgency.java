package com.smarttask.taskallocator.domain.model.task;

/**
 * Value Object (enumerado con lógica) que expresa la urgencia de una tarea.
 *
 * <p>Cada nivel lleva asociado un <em>peso</em> que el dominio usa tanto para
 * calcular la carga que la tarea supone como para priorizar unas tareas frente
 * a otras. La lógica vive en el propio enum, no en un {@code switch} disperso
 * por la aplicación.
 */
public enum TaskUrgency {

    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(5);

    private final int weight;

    TaskUrgency(int weight) {
        this.weight = weight;
    }

    /** Peso de la urgencia; a mayor peso, mayor carga y prioridad. */
    public int weight() {
        return weight;
    }

    public boolean isMoreUrgentThan(TaskUrgency other) {
        return this.weight > other.weight;
    }

    public boolean isCritical() {
        return this == CRITICAL;
    }

    /**
     * Convierte una cadena externa en una urgencia del dominio, de forma
     * tolerante a mayúsculas/minúsculas y espacios.
     *
     * @throws IllegalArgumentException si el valor no corresponde a ningún nivel
     */
    public static TaskUrgency fromString(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("La urgencia no puede estar vacía");
        }
        try {
            return TaskUrgency.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Urgencia desconocida: '" + raw + "'");
        }
    }
}
