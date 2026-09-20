package com.zv.pedidos_service.saga;

import com.zv.pedidos_service.events.PedidoCreadoEvent;

public interface PedidoEventPublisher {
    void publicarPedidoCreado(PedidoCreadoEvent evento);
}
