package com.zv.pagos_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics")
public record KafkaTopicsProperties(
        String stockReservado,
        String pagoAprobado,
        String pagoRechazado
) {
}
