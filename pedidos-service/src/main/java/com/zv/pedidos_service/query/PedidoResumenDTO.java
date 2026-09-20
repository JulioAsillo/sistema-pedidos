package com.zv.pedidos_service.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PedidoResumenDTO(
        UUID pedidoId,
        UUID clienteId,
        String nombreEstado,
        BigDecimal montoTotal,
        Integer cantidadProductos,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimaActualizacion
) {}
