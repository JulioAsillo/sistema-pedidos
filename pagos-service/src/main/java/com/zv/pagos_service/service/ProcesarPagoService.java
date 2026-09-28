package com.zv.pagos_service.service;


import com.zv.pagos_service.domain.EstadoPago;
import com.zv.pagos_service.domain.Pago;
import com.zv.pagos_service.domain.PagoRepository.PagoRepository;
import com.zv.pagos_service.events.PagoAprobadoEvent;
import com.zv.pagos_service.events.PagoRechazadoEvent;
import com.zv.pagos_service.events.StockReservadoEvent;
import com.zv.pagos_service.saga.PagoEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcesarPagoService {

    static final BigDecimal LIMITE_APROBACION = new BigDecimal("1000.00");

    private final PagoRepository pagoRepository;
    private final PagoEventPublisher eventPublisher;

    @Transactional
    public void procesar(StockReservadoEvent evento) {
        if (pagoRepository.existsByPedidoId(evento.pedidoId())) {
            log.info("Pedido {} ya tiene pago registrado - evento duplicado ignorado", evento.pedidoId());
        }

        Pago pago = new Pago();
        pago.setPedidoId(evento.pedidoId());
        pago.setClienteId(evento.clienteId());
        pago.setMonto(evento.montoTotal());

        if (evento.montoTotal().compareTo(LIMITE_APROBACION) > 0) {
            pago.setEstado(EstadoPago.RECHAZADO);
            pago.setMotivo("Monto excede al límite de " + LIMITE_APROBACION);
            pagoRepository.save(pago);
            log.warn("Pago RECHAZADO pedido {}: {}", evento.pedidoId(), pago.getMotivo());
            eventPublisher.publicarPagoRechazado(new PagoRechazadoEvent(
                    evento.pedidoId(), evento.productoId(), evento.cantidad(), pago.getMotivo()));
            return;
        }

        pago.setEstado(EstadoPago.APROBADO);
        Pago guardado = pagoRepository.save(pago);
        log.info("Pago APROBADO pedido {} monto {}", evento.pedidoId(), evento.montoTotal());
        eventPublisher.publicarPagoAprobado(
                new PagoAprobadoEvent(evento.pedidoId(), guardado.getId(), guardado.getMonto()));
    }


}
