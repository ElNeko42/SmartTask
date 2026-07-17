package com.smarttask.taskallocator.application.port.out;

import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.domain.model.member.MemberId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: persistencia del agregado {@link Member}.
 *
 * <p>El dominio y la aplicación dependen de esta abstracción; la
 * implementación concreta (in-memory, JPA, …) vive en la capa de
 * infraestructura, cumpliendo la inversión de dependencias hexagonal.
 */
public interface MemberRepository {

    Optional<Member> findById(MemberId id);

    List<Member> findAll();

    Member save(Member member);
}
