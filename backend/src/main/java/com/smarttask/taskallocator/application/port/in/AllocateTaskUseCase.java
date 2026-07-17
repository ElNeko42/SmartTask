package com.smarttask.taskallocator.application.port.in;

/**
 * Puerto de entrada: recibir una tarea y asignarla automáticamente al miembro
 * más adecuado según las reglas de capacidad del dominio.
 */
public interface AllocateTaskUseCase {

    AllocationResult allocate(SubmitTaskCommand command);
}
