package com.zv.pedidos_service.saga;

import com.zv.pedidos_service.config.KafkaTopicsProperties;
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
        kafkaTemplate.send(topics.pedidoCreado(), evento.pedidoId().toString(), evento)
                .whenComplete((result, ex) -> {
                    if (ex != null){
                        log.error("Error publicando PedidoCreado {}", evento.pedidoId(), ex);
                    } else{
                        log.debug("PedidoCreado {} -> partición {} offset {}",
                                evento.pedidoId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
