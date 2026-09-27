package com.zv.catalogo_service.domain.repository;

import com.zv.catalogo_service.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
}
