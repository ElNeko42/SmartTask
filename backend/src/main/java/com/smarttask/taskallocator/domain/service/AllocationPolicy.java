package com.smarttask.taskallocator.domain.service;

import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.domain.model.task.Task;

import java.util.Collection;
import java.util.Optional;

/**
 * Servicio de dominio que decide <em>a qué</em> miembro se asigna una tarea.
 *
 * <p>Es una estrategia: la política concreta (menos cargado, round-robin, por
 * especialidad…) puede variar sin tocar la aplicación. Vive en el dominio
 * porque "elegir al responsable" es una regla de negocio, no de infraestructura.
 */
public interface AllocationPolicy {

    /**
     * Selecciona el miembro más adecuado para la tarea entre los candidatos,
     * respetando la regla {@link Member#canAccept(Task)}.
     *
     * @return el miembro elegido, o vacío si ninguno puede aceptarla
     */
    Optional<Member> selectMemberFor(Task task, Collection<Member> candidates);
}
