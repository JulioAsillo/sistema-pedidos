package com.zv.pedidos_service.events;

import java.util.UUID;

public record StockRechazadoEvent(
        UUID pedidoId,
        UUID productoId,
        String motivo
) {}