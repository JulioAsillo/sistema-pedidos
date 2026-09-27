package com.zv.catalogo_service.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reservas")
public class Reserva {

    @Id
    private UUID pedidoId;

    @Column(nullable = false)
    private UUID productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    private LocalDateTime fechaActualizacion;

    @PrePersist
    @PreUpdate
    void tocar(){
        this.fechaActualizacion = LocalDateTime.now();
    }

}
