package com.zv.pagos_service.saga;

import com.zv.pagos_service.events.StockReservadoEvent;
import com.zv.pagos_service.service.ProcesarPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockReservadoListener {

    private final ProcesarPagoService procesarPagoService;

    @KafkaListener(
            topics = "${app.kafka.topics.stock-reservado}",
            properties = "spring.json.value.default.type=com.zv.pagos_service.events.StockReservadoEvent"
    )
    public void onStockReservado(StockReservadoEvent evento){
        procesarPagoService.procesar(evento);
    }

}
