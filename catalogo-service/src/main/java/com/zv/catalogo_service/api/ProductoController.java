package com.zv.catalogo_service.api;

import com.zv.catalogo_service.domain.Producto;
import com.zv.catalogo_service.domain.repository.ProductoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository productoRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(
            @Valid
            @RequestBody
            CrearProductoRequest req
    ){
        Producto p = new Producto();
        p.setNombre(req.nombre());
        p.setPrecio(req.precio());
        p.setStock(req.stock());

        return productoRepository.save(p);
    }

    @GetMapping
    public List<Producto> listar(){
        return productoRepository.findAll();
    }

}
