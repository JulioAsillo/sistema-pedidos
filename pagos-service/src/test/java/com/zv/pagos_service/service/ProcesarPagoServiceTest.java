package com.zv.pagos_service.service;

import com.zv.pagos_service.domain.EstadoPago;
import com.zv.pagos_service.domain.Pago;
import com.zv.pagos_service.domain.PagoRepository.PagoRepository;
import com.zv.pagos_service.events.PagoAprobadoEvent;
import com.zv.pagos_service.events.PagoRechazadoEvent;
import com.zv.pagos_service.events.StockReservadoEvent;
import com.zv.pagos_service.saga.PagoEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcesarPagoServiceTest {

    @Mock PagoRepository pagoRepository;
    @Mock PagoEventPublisher eventPublisher;
    @InjectMocks ProcesarPagoService service;

    private StockReservadoEvent evento(String monto) {
        return new StockReservadoEvent(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), 2, new BigDecimal(monto));
    }

    @Test
    void eventoDuplicado_noGuardaNiPublica() {
        var e = evento("100.00");
        when(pagoRepository.existsByPedidoId(e.pedidoId())).thenReturn(true);

        service.procesar(e);

        verify(pagoRepository, never()).saveAndFlush(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void montoDentroDelLimite_apruebaYPublica() {
        var e = evento("500.00");
        when(pagoRepository.existsByPedidoId(e.pedidoId())).thenReturn(false);
        when(pagoRepository.saveAndFlush(any(Pago.class))).thenAnswer(inv -> inv.getArgument(0));

        service.procesar(e);

        var captor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getEstado()).isEqualTo(EstadoPago.APROBADO);
        verify(eventPublisher).publicarPagoAprobado(any(PagoAprobadoEvent.class));
        verify(eventPublisher, never()).publicarPagoRechazado(any());
    }

    @Test
    void montoSobreElLimite_rechazaYPublicaRechazo() {
        var e = evento("1500.00");
        when(pagoRepository.existsByPedidoId(e.pedidoId())).thenReturn(false);

        service.procesar(e);

        verify(eventPublisher).publicarPagoRechazado(any(PagoRechazadoEvent.class));
        verify(eventPublisher, never()).publicarPagoAprobado(any());
    }

    @Test
    void carreraEnInsert_seIgnoraSinPublicar() {
        var e = evento("100.00");
        when(pagoRepository.existsByPedidoId(e.pedidoId())).thenReturn(false);
        when(pagoRepository.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("dup"));

        service.procesar(e);

        verifyNoInteractions(eventPublisher);
    }
}