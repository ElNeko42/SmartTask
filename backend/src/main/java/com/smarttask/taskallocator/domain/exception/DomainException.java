package com.smarttask.taskallocator.domain.exception;

/**
 * Raíz de todas las excepciones del dominio.
 *
 * <p>Permite a las capas externas distinguir un error de reglas de negocio
 * (traducible a un 4xx) de un fallo técnico, sin acoplarse a clases concretas.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
