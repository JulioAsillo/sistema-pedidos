package com.zv.pedidos_service.events;

import java.math.BigDecimal;
import java.util.UUID;

public record PagoAprobadoEvent(
        UUID pedidoId,
        UUID pagoId,
        BigDecimal monto
) {
}
