package com.zv.pedidos_service.domain.repository;

import com.zv.pedidos_service.domain.PedidoResumen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoResumenRepository extends JpaRepository<PedidoResumen, UUID> {
}
