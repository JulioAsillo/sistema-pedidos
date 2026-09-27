package com.zv.catalogo_service.saga;


import com.zv.catalogo_service.events.PagoRechazadoEvent;
import com.zv.catalogo_service.service.ReservaStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagoRechazadoListener {
    private final ReservaStockService reservaStockService;

    @KafkaListener(
            topics = "${app.kafka.topics.pago-rechazado}",
            properties = "spring.json.value.default.type=com.zv.catalogo_service.events.PagoRechazadoEvent"
    )
    public void onPagoRechazado(PagoRechazadoEvent evento){
        reservaStockService.liberar(evento);
    }
}
