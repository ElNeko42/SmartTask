package com.smarttask.taskallocator.application.service;

import com.smarttask.taskallocator.application.port.in.AllocationResult;
import com.smarttask.taskallocator.application.port.in.SubmitTaskCommand;
import com.smarttask.taskallocator.application.port.out.MemberRepository;
import com.smarttask.taskallocator.domain.exception.NoAvailableMemberException;
import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.domain.model.member.MemberId;
import com.smarttask.taskallocator.domain.service.LeastLoadedAllocationPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskAllocationServiceTest {

    private FakeMemberRepository repository;
    private TaskAllocationService service;

    @BeforeEach
    void setUp() {
        repository = new FakeMemberRepository();
        service = new TaskAllocationService(repository, new LeastLoadedAllocationPolicy());
    }

    @Test
    void asigna_la_tarea_al_miembro_menos_cargado_y_lo_persiste() {
        Member ana = repository.save(Member.create("Ana", 8));
        Member beto = repository.save(Member.create("Beto", 5));
        // Ana arranca más cargada para que gane Beto
        ana.assign(taskEffort(6));
        repository.save(ana);

        AllocationResult result = service.allocate(new SubmitTaskCommand("Configurar CI", "HIGH", 3));

        assertThat(result.memberId()).isEqualTo(beto.id());
        assertThat(result.memberCurrentLoad()).isEqualTo(3);
        assertThat(repository.findById(beto.id()).orElseThrow().currentLoad()).isEqualTo(3);
    }

    @Test
    void lanza_excepcion_cuando_ningun_miembro_tiene_capacidad() {
        Member beto = repository.save(Member.create("Beto", 5));
        beto.assign(taskEffort(4));
        repository.save(beto);

        assertThatThrownBy(() -> service.allocate(new SubmitTaskCommand("Deploy", "CRITICAL", 3)))
                .isInstanceOf(NoAvailableMemberException.class);
    }

    @Test
    void propaga_error_de_dominio_ante_una_urgencia_invalida() {
        repository.save(Member.create("Ana", 8));

        assertThatThrownBy(() -> service.allocate(new SubmitTaskCommand("Tarea", "URGENTE", 2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static com.smarttask.taskallocator.domain.model.task.Task taskEffort(int effort) {
        return com.smarttask.taskallocator.domain.model.task.Task.create(
                com.smarttask.taskallocator.domain.model.task.TaskTitle.of("Tarea previa"),
                com.smarttask.taskallocator.domain.model.task.TaskUrgency.MEDIUM,
                effort);
    }

    /** Doble de prueba: repositorio en memoria, sin depender de la infraestructura. */
    private static class FakeMemberRepository implements MemberRepository {
        private final Map<MemberId, Member> store = new LinkedHashMap<>();

        @Override
        public Optional<Member> findById(MemberId id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Member> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public Member save(Member member) {
            store.put(member.id(), member);
            return member;
        }
    }
}
