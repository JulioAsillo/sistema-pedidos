package com.zv.pedidos_service.query;

import com.zv.pedidos_service.domain.PedidoResumen;
import com.zv.pedidos_service.domain.repository.PedidoResumenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoQueryService {

    private final PedidoResumenRepository pedidoResumenRepository;

    public PedidoResumenDTO buscarPorId(UUID id){
        PedidoResumen resumen = pedidoResumenRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado: " + id));
        return aDTO(resumen);
    }

    public List<PedidoResumenDTO> listarTodos(){
        return pedidoResumenRepository.findAll()
                .stream()
                .map(this::aDTO)
                .toList();
    }

    public BigDecimal sumarMontosConfirmados(){
        return pedidoResumenRepository.findAll()
                .stream()
                .filter(p -> "CONFIRMADO".equals(p.getNombreEstado()))
                .map(PedidoResumen::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PedidoResumenDTO aDTO(PedidoResumen resumen){
        return new PedidoResumenDTO(
                resumen.getPedidoId(),
                resumen.getClienteId(),
                resumen.getNombreEstado(),
                resumen.getMontoTotal(),
                resumen.getCantidadProductos(),
                resumen.getFechaCreacion(),
                resumen.getUltimaActualizacion()
        );
    }

}
