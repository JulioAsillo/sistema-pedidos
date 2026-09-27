package com.zv.catalogo_service.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Version
    private Long version;

    public boolean tieneStock(int cantidad){
        return stock >= cantidad;
    }

    public void descontarStock(int cantidad){
        if(!tieneStock(cantidad)){
            throw new IllegalArgumentException("Stock insuficiente para produco " + id + "\n Nombre: " + nombre);
        }
        this.stock -= cantidad;
    }

}
