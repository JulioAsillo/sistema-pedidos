package com.zv.pedidos_service.command;

import com.zv.pedidos_service.domain.Pedido;
import com.zv.pedidos_service.domain.repository.PedidoRepository;
import com.zv.pedidos_service.events.PedidoCreadoEvent;
import com.zv.pedidos_service.saga.PedidoEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrearPedidoHandler {

    private final PedidoRepository pedidoRepository;
    private final PedidoEventPublisher eventPublisher;

    public UUID handle(CrearPedidoCommand command){
        Pedido pedido = new Pedido();
        pedido.setClienteId(command.clienteId());
        pedido.setProductoId(command.productoId());
        pedido.setCantidad(command.cantidad());
        pedido.setMontoTotal(command.montoTotal());

        Pedido guardado = pedidoRepository.save(pedido);
        PedidoCreadoEvent evento = new PedidoCreadoEvent(
                guardado.getId(),
                guardado.getClienteId(),
                guardado.getProductoId(),
                guardado.getCantidad(),
                guardado.getMontoTotal(),
                guardado.getFechaCreacion()
        );
        eventPublisher.publicarPedidoCreado(evento);

        return guardado.getId();
    }
}
