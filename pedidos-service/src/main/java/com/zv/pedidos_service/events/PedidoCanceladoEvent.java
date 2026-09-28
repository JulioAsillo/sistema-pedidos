package com.zv.pedidos_service.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record PedidoCanceladoEvent(
        UUID pedidoId,
        UUID clienteId,
        String motivo,
        LocalDateTime fecha
) {
}
