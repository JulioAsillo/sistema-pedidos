package com.zv.notificaciones_service.listener;


import com.zv.notificaciones_service.events.PedidoCanceladoEvent;
import com.zv.notificaciones_service.events.PedidoConfirmadoEvent;
import com.zv.notificaciones_service.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidosEventsListener {

    private final NotificacionService notificacionService;

    @KafkaListener(
            topics = "${app.kafka.topics.pedido-confirmado}",
            properties = "spring.json.value.default.type=com.zv.notificaciones_service.events.PedidoConfirmadoEvent"
    )
    public void onPedidoConfirmado(PedidoConfirmadoEvent evento){
        notificacionService.notificarConfirmacion(evento);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.pedido-cancelado}",
            properties = "spring.json.value.default.type=com.zv.notificaciones_service.events.PedidoCanceladoEvent"
    )
    public void onPedidoCancelado(PedidoCanceladoEvent evento) {
        notificacionService.notificarCancelacion(evento);
    }

}
