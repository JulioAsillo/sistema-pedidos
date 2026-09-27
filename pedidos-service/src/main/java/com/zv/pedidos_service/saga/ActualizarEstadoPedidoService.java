package com.zv.pedidos_service.saga;


import com.zv.pedidos_service.domain.EstadoPedido;
import com.zv.pedidos_service.domain.Pedido;
import com.zv.pedidos_service.domain.repository.PedidoRepository;
import com.zv.pedidos_service.domain.repository.PedidoResumenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActualizarEstadoPedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoResumenRepository pedidoResumenRepository;

    @Transactional
    public void transicionar(UUID pedidoId, EstadoPedido nuevo, String motivo){
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if(pedido == null){
            log.warn("Evento para pedido inexistente {} (ignorado)", pedidoId);
            return;
        }

        EstadoPedido anterior = pedido.getEstado();
        if(!pedido.transicionarA(nuevo)){
            log.info("Pedido {} ya estaba en {} - evento ignorado", pedidoId, anterior);
            return;
        }

        pedidoResumenRepository.findById(pedidoId).ifPresent(resumen -> {
            resumen.setNombreEstado(nuevo.name());
            resumen.setUltimaActualizacion(LocalDateTime.now());
        });

        log.info("Pedido {}: {} -> {}{}", pedidoId, anterior, nuevo, motivo!= null ? " ("+motivo+")" : "");

    }


}
