package com.zv.catalogo_service.service;

import com.zv.catalogo_service.domain.EstadoReserva;
import com.zv.catalogo_service.domain.Producto;
import com.zv.catalogo_service.domain.Reserva;
import com.zv.catalogo_service.domain.repository.ProductoRepository;
import com.zv.catalogo_service.domain.repository.ReservaRepository;
import com.zv.catalogo_service.events.PagoRechazadoEvent;
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
    private final ReservaRepository reservaRepository;

    @Transactional
    public void reservar(PedidoCreadoEvent evento){
        if (reservaRepository.existsById(evento.pedidoId())){
            log.info("Pedido {} ya tiene reserva - evento duplicado ignorado", evento.pedidoId());
            return;
        }

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
        Reserva reserva = new Reserva();
        reserva.setPedidoId(evento.pedidoId());
        reserva.setProductoId(producto.getId());
        reserva.setCantidad(evento.cantidad());
        reserva.setEstado(EstadoReserva.RESERVADA);
        reservaRepository.save(reserva);
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

    @Transactional
    public void liberar(PagoRechazadoEvent evento){
        Reserva reserva = reservaRepository.findById(evento.pedidoId()).orElse(null);
        if(reserva == null || reserva.getEstado() != EstadoReserva.RESERVADA){
            log.info("Nada que liberar para pedido {} (sin reserva activa)", evento.pedidoId());
            return;
        }

        Producto producto = productoRepository.findById(reserva.getProductoId())
                .orElseThrow(() -> new IllegalStateException("Producto de la reserva no existe: " + reserva.getProductoId()));

        producto.reponerStock(reserva.getCantidad());
        reserva.setEstado(EstadoReserva.LIBERADA);

        log.info("COMPENSACIÓN: pedido {} -> devueltas {} unidades de {} ({})",
                evento.pedidoId(), reserva.getCantidad(), producto.getNombre(), evento.motivo());
    }

}
