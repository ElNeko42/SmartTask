package com.smarttask.taskallocator.infrastructure.adapter.in.web.dto;

import com.smarttask.taskallocator.application.port.in.AllocationResult;

/**
 * Respuesta al asignar una tarea: a quién se asignó y con qué carga queda.
 */
public record AllocationResponse(
        String taskId,
        String memberId,
        String memberName,
        int memberCurrentLoad,
        int memberMaxCapacity) {

    public static AllocationResponse from(AllocationResult result) {
        return new AllocationResponse(
                result.taskId().toString(),
                result.memberId().toString(),
                result.memberName(),
                result.memberCurrentLoad(),
                result.memberMaxCapacity());
    }
}
