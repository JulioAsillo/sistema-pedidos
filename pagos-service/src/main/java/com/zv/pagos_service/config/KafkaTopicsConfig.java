package com.zv.pagos_service.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic pagoAprobadoTopic(KafkaTopicsProperties t){
        return TopicBuilder.name(t.pagoAprobado()).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic pagoRechazadoTopic(KafkaTopicsProperties t){
        return TopicBuilder.name(t.pagoRechazado()).partitions(3).replicas(1).build();
    }

}
