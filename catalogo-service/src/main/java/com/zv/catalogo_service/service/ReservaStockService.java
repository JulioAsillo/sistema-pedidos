package com.zv.catalogo_service.service;

import com.zv.catalogo_service.domain.Producto;
import com.zv.catalogo_service.domain.repository.ProductoRepository;
import com.zv.catalogo_service.events.PedidoCreadoEvent;
import com.zv.catalogo_service.events.StockRechazadoEvent;
import com.zv.catalogo_service.events.StockReservadoEvent;
import com.zv.catalogo_service.saga.CatalogoEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaStockService {

    private final ProductoRepository productoRepository;
    private final CatalogoEventPublisher eventPublisher;

    @Transactional
    public void reservar(PedidoCreadoEvent evento){
        Optional<Producto> encontrado = productoRepository.findById(evento.productoId());

        if(encontrado.isEmpty()){
            rechazar(evento, "Producto no existe");
            return;
        }

        Producto producto = encontrado.get();
        if(!producto.tieneStock(evento.cantidad())){
            rechazar(evento, "Stock insuficiente (disponible: " + producto.getStock() + ")");
            return;
        }

        producto.descontarStock(evento.cantidad());
        log.info("Stock reservado: pedido {} producto {} cantidad {}",
                evento.pedidoId(), producto.getId(), evento.cantidad());

        eventPublisher.publicarStockReservado(new StockReservadoEvent(
                evento.pedidoId(), evento.clienteId(), evento.productoId(),
                evento.cantidad(), evento.montoTotal()));
    }

    private void rechazar(PedidoCreadoEvent evento, String motivo){
        log.warn("Stock rechazado: pedido {} - {}", evento.pedidoId(), motivo);
        eventPublisher.publicarStockRechazado(
                new StockRechazadoEvent(evento.pedidoId(), evento.productoId(), motivo));
    }

}
