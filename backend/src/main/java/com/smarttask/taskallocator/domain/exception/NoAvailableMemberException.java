package com.smarttask.taskallocator.domain.exception;

/**
 * Se lanza cuando ningún miembro del equipo dispone de capacidad para aceptar
 * una tarea entrante.
 */
public class NoAvailableMemberException extends DomainException {

    public NoAvailableMemberException(String message) {
        super(message);
    }
}
