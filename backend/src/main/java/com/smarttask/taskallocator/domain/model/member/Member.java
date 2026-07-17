package com.smarttask.taskallocator.domain.model.member;

import com.smarttask.taskallocator.domain.exception.CapacityExceededException;
import com.smarttask.taskallocator.domain.model.task.Task;
import com.smarttask.taskallocator.domain.model.task.TaskId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agregado raíz que modela a un miembro del equipo y las tareas que tiene
 * asignadas.
 *
 * <p>Su <strong>invariante</strong> es simple pero estricta: la suma de la
 * carga de sus tareas nunca puede superar su capacidad máxima. Toda mutación
 * pasa por métodos del agregado ({@link #assign(Task)} / {@link #unassign(TaskId)}),
 * de modo que ese estado no puede quedar inconsistente desde fuera.
 *
 * <p>La decisión de <em>a quién</em> se asigna una tarea vive aquí, en
 * {@link #canAccept(Task)}, y no en un servicio que consulte la base de datos.
 */
public class Member {

    private final MemberId id;
    private final String name;
    private final int maxCapacity;
    private final List<Task> assignedTasks;

    private Member(MemberId id, String name, int maxCapacity, List<Task> assignedTasks) {
        this.id = Objects.requireNonNull(id, "id");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del miembro no puede estar vacío");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor que 0");
        }
        this.name = name.trim();
        this.maxCapacity = maxCapacity;
        this.assignedTasks = new ArrayList<>(Objects.requireNonNull(assignedTasks, "assignedTasks"));
        if (currentLoad() > maxCapacity) {
            throw new CapacityExceededException(
                    "La carga asignada (" + currentLoad() + ") supera la capacidad máxima ("
                            + maxCapacity + ") de " + this.name);
        }
    }

    /** Crea un miembro nuevo, sin tareas y con identidad recién generada. */
    public static Member create(String name, int maxCapacity) {
        return new Member(MemberId.generate(), name, maxCapacity, List.of());
    }

    /** Rehidrata un miembro existente junto con sus tareas ya asignadas. */
    public static Member restore(MemberId id, String name, int maxCapacity, List<Task> assignedTasks) {
        return new Member(id, name, maxCapacity, assignedTasks);
    }

    /** Carga actual: suma de la carga de todas las tareas asignadas. */
    public int currentLoad() {
        return assignedTasks.stream().mapToInt(Task::workload).sum();
    }

    /** Capacidad libre restante. */
    public int availableCapacity() {
        return maxCapacity - currentLoad();
    }

    /**
     * Regla de negocio principal: ¿puede este miembro aceptar la tarea sin
     * exceder su capacidad? Es una consulta pura, sin efectos secundarios.
     *
     * <p>Una tarea ya asignada no se puede volver a aceptar.
     */
    public boolean canAccept(Task task) {
        Objects.requireNonNull(task, "task");
        if (hasTask(task.id())) {
            return false;
        }
        return currentLoad() + task.workload() <= maxCapacity;
    }

    /**
     * Asigna la tarea al miembro protegiendo la invariante del agregado.
     *
     * @throws CapacityExceededException si el miembro no puede aceptarla
     */
    public void assign(Task task) {
        if (!canAccept(task)) {
            throw new CapacityExceededException(
                    "%s no puede aceptar la tarea %s: carga %d + %d excedería la capacidad %d"
                            .formatted(name, task.id(), currentLoad(), task.workload(), maxCapacity));
        }
        assignedTasks.add(task);
    }

    /** Desasigna una tarea por su id. Devuelve {@code true} si estaba asignada. */
    public boolean unassign(TaskId taskId) {
        Objects.requireNonNull(taskId, "taskId");
        return assignedTasks.removeIf(task -> task.id().equals(taskId));
    }

    public boolean hasTask(TaskId taskId) {
        return assignedTasks.stream().anyMatch(task -> task.id().equals(taskId));
    }

    public MemberId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int maxCapacity() {
        return maxCapacity;
    }

    /** Vista de solo lectura de las tareas asignadas. */
    public List<Task> assignedTasks() {
        return Collections.unmodifiableList(assignedTasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member other)) {
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
        return "Member{id=%s, name=%s, load=%d/%d}"
                .formatted(id, name, currentLoad(), maxCapacity);
    }
}
