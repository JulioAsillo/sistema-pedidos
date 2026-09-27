package com.zv.catalogo_service.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CrearProductoRequest(
        @NotBlank
        String nombre,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal precio,

        @NotNull
        @Min(0)
        Integer stock
) {}
