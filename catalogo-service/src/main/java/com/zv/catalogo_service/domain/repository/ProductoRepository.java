package com.zv.catalogo_service.domain.repository;


import com.zv.catalogo_service.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID> {}
