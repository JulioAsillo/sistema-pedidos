package com.zv.pedidos_service.saga;

import com.zv.pedidos_service.domain.EstadoPedido;
import com.zv.pedidos_service.domain.Pedido;
import com.zv.pedidos_service.domain.PedidoResumen;
import com.zv.pedidos_service.domain.repository.PedidoRepository;
import com.zv.pedidos_service.domain.repository.PedidoResumenRepository;
import com.zv.pedidos_service.events.PedidoCanceladoEvent;
import com.zv.pedidos_service.events.PedidoConfirmadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActualizarEstadoPedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoResumenRepository pedidoResumenRepository;
    private final PedidoEventPublisher eventPublisher;

    @Transactional
    public void transicionar(UUID pedidoId, EstadoPedido nuevo, String motivo) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) {
            log.warn("Evento para pedido inexistente {} (ignorado)", pedidoId);
            return;
        }

        EstadoPedido anterior = pedido.getEstado();
        if (!pedido.transicionarA(nuevo)) {
            log.info("Pedido {} ya estaba en {} - evento ignorado", pedidoId, anterior);
            return;
        }

        actualizarResumen(pedido, nuevo);
        publicarSiEsFinal(pedido, nuevo, motivo);

        log.info("Pedido {}: {} -> {}{}", pedidoId, anterior, nuevo,
                motivo != null ? " (" + motivo + ")" : "");
    }

    private void actualizarResumen(Pedido pedido, EstadoPedido nuevo) {
        PedidoResumen resumen = pedidoResumenRepository.findById(pedido.getId())
                .orElseGet(() -> new PedidoResumen(
                        pedido.getId(), pedido.getClienteId(), null,
                        pedido.getMontoTotal(), pedido.getCantidad(),
                        pedido.getFechaCreacion(), null));
        resumen.setNombreEstado(nuevo.name());
        resumen.setUltimaActualizacion(LocalDateTime.now());
        pedidoResumenRepository.save(resumen);
    }

    private void publicarSiEsFinal(Pedido pedido, EstadoPedido nuevo, String motivo) {
        switch (nuevo) {
            case CONFIRMADO -> eventPublisher.publicarPedidoConfirmado(new PedidoConfirmadoEvent(
                    pedido.getId(), pedido.getClienteId(), pedido.getMontoTotal(), LocalDateTime.now()));
            case CANCELADO -> eventPublisher.publicarPedidoCancelado(new PedidoCanceladoEvent(
                    pedido.getId(), pedido.getClienteId(), motivo, LocalDateTime.now()));
            default -> { /* estados intermedios: no se notifican */ }
        }
    }
}