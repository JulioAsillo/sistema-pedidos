package com.zv.catalogo_service.events;

import java.util.UUID;

public record PagoRechazadoEvent(
        UUID pedidoId,
        UUID productoId,
        Integer cantidad,
        String motivo
) {
}
