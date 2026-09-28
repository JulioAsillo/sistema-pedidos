package com.zv.pedidos_service.saga;

import com.zv.pedidos_service.domain.EstadoPedido;
import com.zv.pedidos_service.events.StockRechazadoEvent;
import com.zv.pedidos_service.events.StockReservadoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CatalogoEventsListener {

    private final ActualizarEstadoPedidoService estadoPedidoService;

    @KafkaListener(
            topics = "${app.kafka.topics.stock-reservado}",
            properties = "spring.json.value.default.type=com.zv.pedidos_service.events.StockReservadoEvent"
    )
    public void onStockReservado(StockReservadoEvent evento) {
        estadoPedidoService.transicionar(evento.pedidoId(), EstadoPedido.STOCK_RESERVADO, null);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.stock-rechazado}",
            properties = "spring.json.value.default.type=com.zv.pedidos_service.events.StockRechazadoEvent"
    )
    public void onStockRechazado(StockRechazadoEvent evento) {
        estadoPedidoService.transicionar(evento.pedidoId(), EstadoPedido.CANCELADO, evento.motivo());
    }

}
