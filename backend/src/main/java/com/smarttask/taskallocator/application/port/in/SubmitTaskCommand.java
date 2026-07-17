package com.smarttask.taskallocator.application.port.in;

/**
 * Datos de una tarea entrante que debe asignarse automáticamente.
 *
 * <p>Se reciben como tipos primitivos/cadena; es el caso de uso quien los
 * traduce a los Value Objects del dominio.
 */
public record SubmitTaskCommand(String title, String urgency, int effort) {
}
