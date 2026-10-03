CREATE TABLE pagos (
                       id              UUID           PRIMARY KEY,
                       pedido_id       UUID           NOT NULL,
                       cliente_id      UUID           NOT NULL,
                       monto           NUMERIC(12,2)  NOT NULL,
                       estado          VARCHAR(30)    NOT NULL,
                       motivo          VARCHAR(255),
                       fecha_procesado TIMESTAMP      NOT NULL,
                       CONSTRAINT uk_pagos_pedido UNIQUE (pedido_id)
);