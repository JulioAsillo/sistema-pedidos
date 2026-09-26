package com.zv.pedidos_service.command;

import com.zv.pedidos_service.domain.Pedido;
import com.zv.pedidos_service.domain.PedidoResumen;
import com.zv.pedidos_service.domain.repository.PedidoRepository;
import com.zv.pedidos_service.domain.repository.PedidoResumenRepository;
import com.zv.pedidos_service.events.PedidoCreadoEvent;
import com.zv.pedidos_service.saga.PedidoEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrearPedidoHandler {

    private final PedidoRepository pedidoRepository;
    private final PedidoResumenRepository pedidoResumenRepository;
    private final PedidoEventPublisher eventPublisher;

    @Transactional
    public UUID handle(CrearPedidoCommand command){
        Pedido pedido = new Pedido();
        pedido.setClienteId(command.clienteId());
        pedido.setProductoId(command.productoId());
        pedido.setCantidad(command.cantidad());
        pedido.setMontoTotal(command.montoTotal());

        Pedido guardado = pedidoRepository.save(pedido);

        PedidoResumen resumen = new PedidoResumen(
                guardado.getId(),
                guardado.getClienteId(),
                guardado.getEstado().name(),
                guardado.getMontoTotal(),
                guardado.getCantidad(),
                guardado.getFechaCreacion(),
                LocalDateTime.now()
        );
        pedidoResumenRepository.save(resumen);

        PedidoCreadoEvent evento = new PedidoCreadoEvent(
          guardado.getId(), guardado.getClienteId(), guardado.getProductoId(),
          guardado.getCantidad(), guardado.getMontoTotal(), guardado.getFechaCreacion());
        eventPublisher.publicarPedidoCreado(evento);

        return guardado.getId();
    }

}
