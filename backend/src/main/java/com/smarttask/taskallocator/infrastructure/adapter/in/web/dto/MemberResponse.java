package com.smarttask.taskallocator.infrastructure.adapter.in.web.dto;

import com.smarttask.taskallocator.domain.model.member.Member;

import java.util.List;

/**
 * Representación de un miembro y su carga de cara al cliente.
 */
public record MemberResponse(
        String id,
        String name,
        int maxCapacity,
        int currentLoad,
        int availableCapacity,
        List<TaskResponse> tasks) {

    public static MemberResponse from(Member member) {
        List<TaskResponse> tasks = member.assignedTasks().stream()
                .map(TaskResponse::from)
                .toList();
        return new MemberResponse(
                member.id().toString(),
                member.name(),
                member.maxCapacity(),
                member.currentLoad(),
                member.availableCapacity(),
                tasks);
    }
}
