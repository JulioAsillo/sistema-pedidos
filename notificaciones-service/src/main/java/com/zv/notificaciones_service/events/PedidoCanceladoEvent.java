package com.zv.notificaciones_service.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PedidoCanceladoEvent(
        UUID pedidoId,
        UUID clienteId,
        String motivo,
        LocalDateTime fecha
) {
}
