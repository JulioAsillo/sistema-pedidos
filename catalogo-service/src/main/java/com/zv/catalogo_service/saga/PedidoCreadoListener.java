package com.zv.catalogo_service.saga;


import com.zv.catalogo_service.events.PedidoCreadoEvent;
import com.zv.catalogo_service.service.ReservaStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCreadoListener {

    private final ReservaStockService reservaStockService;

    @KafkaListener(
            topics = "${app.kafka.topics.pedido-creado}",
            properties = "spring.json.value.default.type=com.zv.catalogo_service.events.PedidoCreadoEvent"
    )
    public void onPedidoCreado(PedidoCreadoEvent evento){
        log.debug("Recibido PedidoCreado {}", evento.pedidoId());
        reservaStockService.reservar(evento);
    }

}
