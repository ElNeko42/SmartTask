package com.smarttask.taskallocator.domain.exception;

/**
 * Se lanza cuando se intenta asignar una tarea a un miembro que no dispone de
 * capacidad suficiente, violando la invariante del agregado {@code Member}.
 */
public class CapacityExceededException extends DomainException {

    public CapacityExceededException(String message) {
        super(message);
    }
}
