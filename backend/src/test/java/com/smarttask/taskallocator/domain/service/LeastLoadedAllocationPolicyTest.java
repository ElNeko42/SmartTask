package com.smarttask.taskallocator.domain.service;

import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.domain.model.task.Task;
import com.smarttask.taskallocator.domain.model.task.TaskTitle;
import com.smarttask.taskallocator.domain.model.task.TaskUrgency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LeastLoadedAllocationPolicyTest {

    private final AllocationPolicy policy = new LeastLoadedAllocationPolicy();

    private static Task task(int effort) {
        return Task.create(TaskTitle.of("Tarea de ejemplo"), TaskUrgency.HIGH, effort);
    }

    @Test
    void elige_al_miembro_con_menor_carga() {
        Member ana = Member.create("Ana", 10);
        Member beto = Member.create("Beto", 10);
        ana.assign(task(6)); // Ana más cargada

        var elegido = policy.selectMemberFor(task(2), List.of(ana, beto));

        assertThat(elegido).contains(beto);
    }

    @Test
    void ante_empate_de_carga_prefiere_al_de_mas_capacidad_libre() {
        Member ana = Member.create("Ana", 10);  // libre 10
        Member beto = Member.create("Beto", 5);  // libre 5

        var elegido = policy.selectMemberFor(task(3), List.of(beto, ana));

        assertThat(elegido).contains(ana);
    }

    @Test
    void descarta_a_los_que_no_tienen_capacidad() {
        Member ana = Member.create("Ana", 4);
        ana.assign(task(3)); // libre 1, no cabe effort 2

        var elegido = policy.selectMemberFor(task(2), List.of(ana));

        assertThat(elegido).isEmpty();
    }

    @Test
    void sin_candidatos_no_hay_seleccion() {
        var elegido = policy.selectMemberFor(task(2), List.of());

        assertThat(elegido).isEmpty();
    }
}
