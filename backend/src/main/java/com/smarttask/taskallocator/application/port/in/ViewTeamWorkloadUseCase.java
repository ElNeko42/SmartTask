package com.smarttask.taskallocator.application.port.in;

import com.smarttask.taskallocator.domain.model.member.Member;

import java.util.List;

/**
 * Puerto de entrada (consulta): ver el estado de carga del equipo.
 */
public interface ViewTeamWorkloadUseCase {

    List<Member> currentWorkload();
}
