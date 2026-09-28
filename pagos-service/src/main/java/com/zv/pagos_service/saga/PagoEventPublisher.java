package com.zv.pagos_service.saga;

import com.zv.pagos_service.events.PagoAprobadoEvent;
import com.zv.pagos_service.events.PagoRechazadoEvent;

public interface PagoEventPublisher {
    void publicarPagoAprobado(PagoAprobadoEvent evento);
    void publicarPagoRechazado(PagoRechazadoEvent evento);
}
