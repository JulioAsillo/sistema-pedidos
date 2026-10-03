CREATE TABLE productos (
                           id      UUID           PRIMARY KEY,
                           nombre  VARCHAR(255)   NOT NULL,
                           precio  NUMERIC(12,2)  NOT NULL CHECK (precio >= 0),
                           stock   INTEGER        NOT NULL CHECK (stock >= 0),
                           version BIGINT
);

CREATE TABLE reservas (
                          pedido_id           UUID        PRIMARY KEY,
                          producto_id         UUID        NOT NULL REFERENCES productos (id),
                          cantidad            INTEGER     NOT NULL CHECK (cantidad > 0),
                          estado              VARCHAR(30) NOT NULL,
                          fecha_actualizacion TIMESTAMP
);