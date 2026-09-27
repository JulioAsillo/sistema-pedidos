package com.zv.catalogo_service.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductoTest {

    private Producto conStock(int stock) {
        Producto p = new Producto();
        p.setStock(stock);
        return p;
    }

    @Test
    @DisplayName("descontarStock resta la cantidad")
    void descuenta() {
        Producto p = conStock(5);
        p.descontarStock(3);
        assertThat(p.getStock()).isEqualTo(2);
    }

    @Test
    @DisplayName("descontarStock lanza excepción si no alcanza y no modifica el stock")
    void noAlcanza() {
        Producto p = conStock(2);

        assertThatThrownBy(() -> p.descontarStock(3))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Stock insuficiente");

        assertThat(p.getStock()).isEqualTo(2);
    }
}