package com.zv.pagos_service.domain.PagoRepository;

import com.zv.pagos_service.domain.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PagoRepository extends JpaRepository<Pago, UUID> {
    boolean existsByPedidoId(UUID pedidoId);
}
