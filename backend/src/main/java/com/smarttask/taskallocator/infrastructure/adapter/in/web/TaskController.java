package com.smarttask.taskallocator.infrastructure.adapter.in.web;

import com.smarttask.taskallocator.application.port.in.AllocateTaskUseCase;
import com.smarttask.taskallocator.application.port.in.AllocationResult;
import com.smarttask.taskallocator.application.port.in.SubmitTaskCommand;
import com.smarttask.taskallocator.infrastructure.adapter.in.web.dto.AllocationResponse;
import com.smarttask.taskallocator.infrastructure.adapter.in.web.dto.SubmitTaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de entrada REST para el envío de tareas.
 *
 * <p>Enviar una tarea dispara su asignación automática al miembro más adecuado.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final AllocateTaskUseCase allocateTask;

    public TaskController(AllocateTaskUseCase allocateTask) {
        this.allocateTask = allocateTask;
    }

    @PostMapping
    public ResponseEntity<AllocationResponse> submit(@Valid @RequestBody SubmitTaskRequest request) {
        AllocationResult result = allocateTask.allocate(
                new SubmitTaskCommand(request.title(), request.urgency(), request.effort()));
        return ResponseEntity.status(HttpStatus.CREATED).body(AllocationResponse.from(result));
    }
}
