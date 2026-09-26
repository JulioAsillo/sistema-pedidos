package com.zv.pedidos_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic pedidoCreadoTopic(KafkaTopicsProperties topics){
        return TopicBuilder.name(topics.pedidoCreado()).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic pedidoCanceladoTopic(KafkaTopicsProperties topics){
        return TopicBuilder.name(topics.pedidoCancelado()).partitions(3).replicas(1).build();
    }

}
