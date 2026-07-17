package com.smarttask.taskallocator.application.port.in;

import com.smarttask.taskallocator.domain.model.member.Member;

/**
 * Puerto de entrada: dar de alta a un nuevo miembro del equipo.
 */
public interface RegisterMemberUseCase {

    Member register(RegisterMemberCommand command);
}
