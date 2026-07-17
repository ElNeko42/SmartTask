package com.smarttask.taskallocator.domain.service;

import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.domain.model.task.Task;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * Política que asigna la tarea al miembro con <strong>menor carga actual</strong>
 * de entre los que pueden aceptarla.
 *
 * <p>En caso de empate, prefiere al que tenga más capacidad libre, repartiendo
 * así el trabajo de forma equilibrada.
 */
public class LeastLoadedAllocationPolicy implements AllocationPolicy {

    @Override
    public Optional<Member> selectMemberFor(Task task, Collection<Member> candidates) {
        return candidates.stream()
                .filter(member -> member.canAccept(task))
                .min(Comparator.comparingInt(Member::currentLoad)
                        .thenComparing(Comparator.comparingInt(Member::availableCapacity).reversed()));
    }
}
