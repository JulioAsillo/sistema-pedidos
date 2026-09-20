package com.zv.pedidos_service.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CrearPedidoCommand(
   @NotNull(message = "El clienteId es obligatorio")
   UUID clienteId,

   @NotNull(message = "El productoId es obligatorio")
   UUID productoId,

   @NotNull(message = "La cantidad es obligatoria")
   @Min(value = 1, message = "La cantidad debe ser mayor a 0")
   Integer cantidad,

   @NotNull(message = "El montoTotal es obligatorio")
   BigDecimal montoTotal

) {}
