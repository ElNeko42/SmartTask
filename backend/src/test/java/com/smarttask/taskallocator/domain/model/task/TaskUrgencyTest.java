package com.smarttask.taskallocator.domain.model.task;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskUrgencyTest {

    @Test
    void critical_es_mas_urgente_que_low() {
        assertThat(TaskUrgency.CRITICAL.isMoreUrgentThan(TaskUrgency.LOW)).isTrue();
        assertThat(TaskUrgency.LOW.isMoreUrgentThan(TaskUrgency.CRITICAL)).isFalse();
    }

    @Test
    void el_peso_crece_con_la_urgencia() {
        assertThat(TaskUrgency.LOW.weight()).isLessThan(TaskUrgency.HIGH.weight());
        assertThat(TaskUrgency.CRITICAL.isCritical()).isTrue();
    }

    @Test
    void se_construye_desde_texto_ignorando_mayusculas_y_espacios() {
        assertThat(TaskUrgency.fromString("  high ")).isEqualTo(TaskUrgency.HIGH);
    }

    @Test
    void rechaza_una_urgencia_desconocida() {
        assertThatThrownBy(() -> TaskUrgency.fromString("URGENTE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Urgencia desconocida");
    }
}
