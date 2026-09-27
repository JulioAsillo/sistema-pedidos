package com.zv.catalogo_service.saga;


import com.zv.catalogo_service.config.KafkaTopicsProperties;
import com.zv.catalogo_service.events.StockRechazadoEvent;
import com.zv.catalogo_service.events.StockReservadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCatalogoEventPublisher implements CatalogoEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    @Override
    public void publicarStockReservado(StockReservadoEvent evento) {
        enviar(topics.stockReservado(), evento.pedidoId().toString(), evento);
    }

    @Override
    public void publicarStockRechazado(StockRechazadoEvent evento) {
        enviar(topics.stockRechazado(), evento.pedidoId().toString(), evento);
    }

    private void enviar(String topic, String key, Object evento){
        kafkaTemplate.send(topic, key, evento).whenComplete((result, ex) ->{
            if (ex != null){
                log.error("Error publicando en {} (key={})", topic, key, ex);
            } else {
                log.debug("Publicado en {} key={} partición {} offset {}", topic, key,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }

}
