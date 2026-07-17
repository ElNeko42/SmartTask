package com.smarttask.taskallocator.application.port.in;

import com.smarttask.taskallocator.domain.model.member.MemberId;
import com.smarttask.taskallocator.domain.model.task.TaskId;

/**
 * Resultado de asignar una tarea: quién la recibió y con qué carga queda.
 */
public record AllocationResult(
        TaskId taskId,
        MemberId memberId,
        String memberName,
        int memberCurrentLoad,
        int memberMaxCapacity) {
}
