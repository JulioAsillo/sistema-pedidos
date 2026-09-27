package com.zv.catalogo_service.saga;

import com.zv.catalogo_service.events.StockRechazadoEvent;
import com.zv.catalogo_service.events.StockReservadoEvent;

public interface CatalogoEventPublisher {
    void publicarStockReservado(StockReservadoEvent evento);
    void publicarStockRechazado(StockRechazadoEvent evento);
}
