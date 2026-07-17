package com.smarttask.taskallocator.domain.model.task;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskTitleTest {

    @Test
    void recorta_los_espacios_del_titulo() {
        assertThat(TaskTitle.of("  Configurar CI  ").value()).isEqualTo("Configurar CI");
    }

    @Test
    void rechaza_un_titulo_vacio_o_en_blanco() {
        assertThatThrownBy(() -> TaskTitle.of("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede estar vacío");
    }

    @Test
    void rechaza_un_titulo_demasiado_corto() {
        assertThatThrownBy(() -> TaskTitle.of("ab"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rechaza_un_titulo_demasiado_largo() {
        assertThatThrownBy(() -> TaskTitle.of("x".repeat(121)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void dos_titulos_con_el_mismo_valor_son_iguales() {
        assertThat(TaskTitle.of("Diseñar API")).isEqualTo(TaskTitle.of("Diseñar API"));
    }
}
