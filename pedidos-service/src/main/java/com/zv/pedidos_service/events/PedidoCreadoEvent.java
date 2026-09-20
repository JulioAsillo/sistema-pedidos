package com.zv.pedidos_service.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PedidoCreadoEvent(
        UUID pedidoId,
        UUID clienteId,
        UUID productoId,
        Integer cantidad,
        BigDecimal montoTotal,
        LocalDateTime fechaCreacion
) {}
