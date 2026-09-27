package com.zv.pedidos_service.saga;


import com.zv.pedidos_service.domain.EstadoPedido;
import com.zv.pedidos_service.events.PagoAprobadoEvent;
import com.zv.pedidos_service.events.PagoRechazadoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagosEventsListener {

    private final ActualizarEstadoPedidoService estadoService;

    @KafkaListener(
            topics = "${app.kafka.topics.pago-aprobado}",
            properties = "spring.json.value.default.type=com.zv.pedidos_service.events.PagoAprobadoEvent"
    )
    public void onPagoAprobado(PagoAprobadoEvent evento) {
        estadoService.transicionar(evento.pedidoId(), EstadoPedido.CONFIRMADO, null);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.pago-rechazado}",
            properties = "spring.json.value.default.type=com.zv.pedidos_service.events.PagoRechazadoEvent"
    )
    public void onPagoRechazado(PagoRechazadoEvent evento) {
        estadoService.transicionar(evento.pedidoId(), EstadoPedido.CANCELADO, evento.motivo());
    }

}
