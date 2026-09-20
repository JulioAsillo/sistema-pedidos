package com.zv.pedidos_service.command;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoCommandController {

    private final CrearPedidoHandler crearPedidoHandler;

    @PostMapping
    public ResponseEntity<Map<String, UUID>> crear(
            @Valid
            @RequestBody
            CrearPedidoCommand command){
        UUID id = crearPedidoHandler.handle(command);
        return ResponseEntity
                .created(URI.create("/pedidos/" + id))
                .body(Map.of("id", id));
    }
}
