package com.smarttask.taskallocator.application.service;

import com.smarttask.taskallocator.application.port.in.AllocateTaskUseCase;
import com.smarttask.taskallocator.application.port.in.AllocationResult;
import com.smarttask.taskallocator.application.port.in.SubmitTaskCommand;
import com.smarttask.taskallocator.application.port.out.MemberRepository;
import com.smarttask.taskallocator.domain.exception.NoAvailableMemberException;
import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.domain.model.task.Task;
import com.smarttask.taskallocator.domain.model.task.TaskTitle;
import com.smarttask.taskallocator.domain.model.task.TaskUrgency;
import com.smarttask.taskallocator.domain.service.AllocationPolicy;

import java.util.List;
import java.util.Objects;

/**
 * Caso de uso central: recibe una tarea, la construye como agregado de dominio
 * y la asigna al miembro que la política de dominio considere más adecuado.
 *
 * <p>El servicio se limita a orquestar: traduce el comando, carga candidatos,
 * delega la decisión en {@link AllocationPolicy} y persiste el resultado. Toda
 * la regla de negocio vive en el dominio.
 */
public class TaskAllocationService implements AllocateTaskUseCase {

    private final MemberRepository memberRepository;
    private final AllocationPolicy allocationPolicy;

    public TaskAllocationService(MemberRepository memberRepository, AllocationPolicy allocationPolicy) {
        this.memberRepository = Objects.requireNonNull(memberRepository);
        this.allocationPolicy = Objects.requireNonNull(allocationPolicy);
    }

    @Override
    public AllocationResult allocate(SubmitTaskCommand command) {
        Task task = Task.create(
                TaskTitle.of(command.title()),
                TaskUrgency.fromString(command.urgency()),
                command.effort());

        List<Member> candidates = memberRepository.findAll();

        Member chosen = allocationPolicy.selectMemberFor(task, candidates)
                .orElseThrow(() -> new NoAvailableMemberException(
                        "Ningún miembro dispone de capacidad para la tarea '" + task.title() + "'"));

        chosen.assign(task);
        memberRepository.save(chosen);

        return new AllocationResult(
                task.id(),
                chosen.id(),
                chosen.name(),
                chosen.currentLoad(),
                chosen.maxCapacity());
    }
}
