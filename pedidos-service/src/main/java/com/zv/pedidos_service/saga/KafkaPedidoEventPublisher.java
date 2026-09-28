package com.zv.pedidos_service.saga;

import com.zv.pedidos_service.config.KafkaTopicsProperties;
import com.zv.pedidos_service.events.PedidoCanceladoEvent;
import com.zv.pedidos_service.events.PedidoConfirmadoEvent;
import com.zv.pedidos_service.events.PedidoCreadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPedidoEventPublisher implements PedidoEventPublisher{

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;


    @Override
    public void publicarPedidoCreado(PedidoCreadoEvent evento) {
        enviar(topics.pedidoCreado(), evento.pedidoId().toString(), evento);
    }

    @Override
    public void publicarPedidoConfirmado(PedidoConfirmadoEvent evento) {
        enviar(topics.pedidoConfirmado(), evento.pedidoId().toString(), evento);
    }

    @Override
    public void publicarPedidoCancelado(PedidoCanceladoEvent evento) {
        enviar(topics.pedidoCancelado(), evento.pedidoId().toString(), evento);
    }

    private void enviar(String topic, String key, Object evento){
        kafkaTemplate.send(topic, key, evento).whenComplete((r, ex) -> {
            if (ex != null) log.error("Error publicando en {} (key={})", topic, key, ex);
            else log.debug("Publicado en {} key={} partición {} offset {}", topic, key,
                    r.getRecordMetadata().partition(), r.getRecordMetadata().offset());
        });
    }
}
