package com.zv.pagos_service.api;

import com.zv.pagos_service.domain.Pago;
import com.zv.pagos_service.domain.PagoRepository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoRepository pagoRepository;

    @GetMapping
    public List<Pago> listar(){
        return pagoRepository.findAll();
    }


}
