package com.zv.catalogo_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic stockReservadoTopic(KafkaTopicsProperties topics){
        return TopicBuilder.name(topics.stockReservado()).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic stockRechazadoTopic(KafkaTopicsProperties topics){
        return TopicBuilder.name(topics.stockRechazado()).partitions(3).replicas(1).build();
    }
}
