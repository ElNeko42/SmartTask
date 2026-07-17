package com.smarttask.taskallocator.infrastructure.adapter.in.web.dto;

import com.smarttask.taskallocator.domain.model.task.Task;

/**
 * Representación de una tarea asignada de cara al cliente.
 */
public record TaskResponse(String id, String title, String urgency, int effort) {

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.id().toString(),
                task.title().value(),
                task.urgency().name(),
                task.effort());
    }
}
