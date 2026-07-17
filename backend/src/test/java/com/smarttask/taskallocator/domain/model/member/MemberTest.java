package com.smarttask.taskallocator.domain.model.member;

import com.smarttask.taskallocator.domain.exception.CapacityExceededException;
import com.smarttask.taskallocator.domain.model.task.Task;
import com.smarttask.taskallocator.domain.model.task.TaskTitle;
import com.smarttask.taskallocator.domain.model.task.TaskUrgency;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    private static Task task(int effort) {
        return Task.create(TaskTitle.of("Tarea de ejemplo"), TaskUrgency.MEDIUM, effort);
    }

    @Test
    void un_miembro_nuevo_no_tiene_carga() {
        Member ana = Member.create("Ana", 8);

        assertThat(ana.currentLoad()).isZero();
        assertThat(ana.availableCapacity()).isEqualTo(8);
    }

    @Test
    void puede_aceptar_una_tarea_que_cabe_en_su_capacidad() {
        Member ana = Member.create("Ana", 8);

        assertThat(ana.canAccept(task(5))).isTrue();
    }

    @Test
    void no_puede_aceptar_una_tarea_que_excede_su_capacidad() {
        Member beto = Member.create("Beto", 5);
        beto.assign(task(3));

        assertThat(beto.canAccept(task(3))).isFalse();
    }

    @Test
    void asignar_aumenta_la_carga_y_reduce_la_capacidad_disponible() {
        Member ana = Member.create("Ana", 8);

        ana.assign(task(3));

        assertThat(ana.currentLoad()).isEqualTo(3);
        assertThat(ana.availableCapacity()).isEqualTo(5);
    }

    @Test
    void asignar_por_encima_de_la_capacidad_lanza_excepcion() {
        Member beto = Member.create("Beto", 5);
        beto.assign(task(4));

        assertThatThrownBy(() -> beto.assign(task(3)))
                .isInstanceOf(CapacityExceededException.class);
    }

    @Test
    void no_acepta_dos_veces_la_misma_tarea() {
        Member ana = Member.create("Ana", 10);
        Task t = task(2);
        ana.assign(t);

        assertThat(ana.canAccept(t)).isFalse();
    }

    @Test
    void desasignar_libera_capacidad() {
        Member ana = Member.create("Ana", 8);
        Task t = task(3);
        ana.assign(t);

        boolean removed = ana.unassign(t.id());

        assertThat(removed).isTrue();
        assertThat(ana.currentLoad()).isZero();
    }
}
