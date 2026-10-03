CREATE TABLE pedidos (
                         id                  UUID           PRIMARY KEY,
                         cliente_id          UUID           NOT NULL,
                         producto_id         UUID           NOT NULL,
                         cantidad            INTEGER        NOT NULL CHECK (cantidad > 0),
                         monto_total         NUMERIC(12,2)  NOT NULL,
                         estado              VARCHAR(30)    NOT NULL,
                         fecha_creacion      TIMESTAMP      NOT NULL,
                         fecha_actualizacion TIMESTAMP
);

CREATE INDEX idx_pedidos_cliente ON pedidos (cliente_id);
CREATE INDEX idx_pedidos_estado  ON pedidos (estado);

CREATE TABLE pedido_resumen (
                                pedido_id            UUID           PRIMARY KEY,
                                cliente_id           UUID,
                                nombre_estado        VARCHAR(30),
                                monto_total          NUMERIC(12,2),
                                cantidad_productos   INTEGER,
                                fecha_creacion       TIMESTAMP,
                                ultima_actualizacion TIMESTAMP
);

CREATE INDEX idx_resumen_cliente ON pedido_resumen (cliente_id);