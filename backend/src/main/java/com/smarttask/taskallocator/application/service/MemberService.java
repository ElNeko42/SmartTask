package com.smarttask.taskallocator.application.service;

import com.smarttask.taskallocator.application.port.in.RegisterMemberCommand;
import com.smarttask.taskallocator.application.port.in.RegisterMemberUseCase;
import com.smarttask.taskallocator.application.port.in.ViewTeamWorkloadUseCase;
import com.smarttask.taskallocator.application.port.out.MemberRepository;
import com.smarttask.taskallocator.domain.model.member.Member;

import java.util.List;
import java.util.Objects;

/**
 * Servicio de aplicación que orquesta los casos de uso relativos a miembros.
 *
 * <p>No contiene lógica de negocio: crea agregados y delega en el repositorio.
 * Es una clase pura (sin dependencias de framework); su cableado como bean se
 * hace en la capa de infraestructura.
 */
public class MemberService implements RegisterMemberUseCase, ViewTeamWorkloadUseCase {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = Objects.requireNonNull(memberRepository);
    }

    @Override
    public Member register(RegisterMemberCommand command) {
        Member member = Member.create(command.name(), command.maxCapacity());
        return memberRepository.save(member);
    }

    @Override
    public List<Member> currentWorkload() {
        return memberRepository.findAll();
    }
}
