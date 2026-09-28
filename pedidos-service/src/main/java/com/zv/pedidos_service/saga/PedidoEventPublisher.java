package com.zv.pedidos_service.saga;

import com.zv.pedidos_service.events.PedidoCanceladoEvent;
import com.zv.pedidos_service.events.PedidoConfirmadoEvent;
import com.zv.pedidos_service.events.PedidoCreadoEvent;

public interface PedidoEventPublisher {
    void publicarPedidoCreado(PedidoCreadoEvent evento);
    void publicarPedidoConfirmado(PedidoConfirmadoEvent evento);
    void publicarPedidoCancelado(PedidoCanceladoEvent evento);
}
