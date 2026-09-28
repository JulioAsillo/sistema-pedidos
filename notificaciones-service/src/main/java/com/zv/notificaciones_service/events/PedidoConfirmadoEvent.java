package com.zv.notificaciones_service.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PedidoConfirmadoEvent(
        UUID pedidoId,
        UUID clienteId,
        BigDecimal montoTotal,
        LocalDateTime fecha
) {
}
