package com.zv.catalogo_service.events;

import java.math.BigDecimal;
import java.util.UUID;

public record StockReservadoEvent(
   UUID pedidoId,
   UUID clienteId,
   UUID productoId,
   Integer cantidad,
   BigDecimal montoTotal
) {}
