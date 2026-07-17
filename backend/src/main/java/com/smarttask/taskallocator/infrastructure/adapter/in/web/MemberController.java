package com.smarttask.taskallocator.infrastructure.adapter.in.web;

import com.smarttask.taskallocator.application.port.in.RegisterMemberCommand;
import com.smarttask.taskallocator.application.port.in.RegisterMemberUseCase;
import com.smarttask.taskallocator.application.port.in.ViewTeamWorkloadUseCase;
import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.infrastructure.adapter.in.web.dto.MemberResponse;
import com.smarttask.taskallocator.infrastructure.adapter.in.web.dto.RegisterMemberRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Adaptador de entrada REST para la gestión de miembros del equipo.
 *
 * <p>Depende únicamente de los puertos de entrada (casos de uso), no de sus
 * implementaciones concretas.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final RegisterMemberUseCase registerMember;
    private final ViewTeamWorkloadUseCase viewTeamWorkload;

    public MemberController(RegisterMemberUseCase registerMember,
                            ViewTeamWorkloadUseCase viewTeamWorkload) {
        this.registerMember = registerMember;
        this.viewTeamWorkload = viewTeamWorkload;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody RegisterMemberRequest request) {
        Member member = registerMember.register(
                new RegisterMemberCommand(request.name(), request.maxCapacity()));
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberResponse.from(member));
    }

    @GetMapping
    public List<MemberResponse> list() {
        return viewTeamWorkload.currentWorkload().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
