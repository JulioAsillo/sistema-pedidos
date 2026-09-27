package com.zv.pagos_service.saga;


import com.zv.pagos_service.config.KafkaTopicsProperties;
import com.zv.pagos_service.events.PagoAprobadoEvent;
import com.zv.pagos_service.events.PagoRechazadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPagoEventPublisher implements PagoEventPublisher{

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;


    @Override
    public void publicarPagoAprobado(PagoAprobadoEvent evento) {
        enviar(topics.pagoAprobado(), evento.pedidoId().toString(), evento);
    }

    @Override
    public void publicarPagoRechazado(PagoRechazadoEvent evento) {
        enviar(topics.pagoRechazado(), evento.pedidoId().toString(), evento);
    }

    private void enviar(String topic, String key, Object evento){
        kafkaTemplate.send(topic, key, evento).whenComplete((r, ex) -> {
           if (ex != null) log.error("Error publicando en {} (key={})", topic, key, ex);
           else log.debug("Publicado en {} key={} offset={}", topic, key, r.getRecordMetadata().offset())
                   ;
        });
    }
}
