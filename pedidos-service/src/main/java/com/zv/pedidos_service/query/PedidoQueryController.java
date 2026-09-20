package com.zv.pedidos_service.query;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoQueryController {

    private final PedidoQueryService pedidoQueryService;

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResumenDTO> obtenerPorId(@PathVariable UUID id){
        return ResponseEntity.ok(pedidoQueryService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResumenDTO>> listarTodos(){
        return ResponseEntity.ok(pedidoQueryService.listarTodos());
    }

    @GetMapping("/total-confirmados")
    public ResponseEntity<BigDecimal> totalConfirmados(){
        return ResponseEntity.ok(pedidoQueryService.sumarMontosConfirmados());
    }

}
