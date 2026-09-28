package com.zv.notificaciones_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topicsv")
public record KafkaTopicsProperties(
        String pedidoConfirmado,
        String pedidoCancelado
) {}