package com.zv.pedidos_service.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedido_resumen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResumen {

    @Id
    private UUID pedidoId;

    private UUID clienteId;

    private String nombreEstado;

    private BigDecimal montoTotal;

    private Integer cantidadProductos;

    private LocalDateTime fechaCreacion;

    private LocalDateTime ultimaActualizacion;

}
