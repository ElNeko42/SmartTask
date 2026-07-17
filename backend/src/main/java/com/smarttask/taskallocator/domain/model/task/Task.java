package com.smarttask.taskallocator.domain.model.task;

import java.util.Objects;

/**
 * Entidad que representa una tarea a asignar.
 *
 * <p>Como toda entidad de DDD, su igualdad se define por <strong>identidad</strong>
 * ({@link TaskId}) y no por sus atributos: dos tareas con el mismo título y
 * urgencia siguen siendo tareas distintas.
 */
public class Task {

    /** Límite superior de esfuerzo razonable para una única tarea. */
    private static final int MAX_EFFORT = 13;

    private final TaskId id;
    private final TaskTitle title;
    private final TaskUrgency urgency;
    private final int effort;

    private Task(TaskId id, TaskTitle title, TaskUrgency urgency, int effort) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Objects.requireNonNull(title, "title");
        this.urgency = Objects.requireNonNull(urgency, "urgency");
        if (effort <= 0) {
            throw new IllegalArgumentException("El esfuerzo debe ser mayor que 0");
        }
        if (effort > MAX_EFFORT) {
            throw new IllegalArgumentException(
                    "El esfuerzo no puede superar " + MAX_EFFORT + " puntos");
        }
        this.effort = effort;
    }

    /** Crea una tarea nueva con identidad recién generada. */
    public static Task create(TaskTitle title, TaskUrgency urgency, int effort) {
        return new Task(TaskId.generate(), title, urgency, effort);
    }

    /** Rehidrata una tarea existente (por ejemplo, desde persistencia). */
    public static Task restore(TaskId id, TaskTitle title, TaskUrgency urgency, int effort) {
        return new Task(id, title, urgency, effort);
    }

    /**
     * Carga que la tarea impone sobre la capacidad de un miembro.
     *
     * <p>De momento coincide con el esfuerzo estimado; se expone como método
     * para que la regla de capacidad no dependa de la representación interna.
     */
    public int workload() {
        return effort;
    }

    public TaskId id() {
        return id;
    }

    public TaskTitle title() {
        return title;
    }

    public TaskUrgency urgency() {
        return urgency;
    }

    public int effort() {
        return effort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task other)) {
            return false;
        }
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Task{id=%s, title=%s, urgency=%s, effort=%d}"
                .formatted(id, title, urgency, effort);
    }
}
