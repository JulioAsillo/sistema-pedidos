package com.zv.pedidos_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics")
public record KafkaTopicsProperties(
    String pedidoCreado,
    String pedidoCancelado,
    String stockReservado,
    String stockRechazado,
    String pagoAprobado,
    String pagoRechazado
) {}
