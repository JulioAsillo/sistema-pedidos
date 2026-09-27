package com.zv.catalogo_service.service;

import com.zv.catalogo_service.domain.Producto;
import com.zv.catalogo_service.domain.repository.ProductoRepository;
import com.zv.catalogo_service.domain.repository.ReservaRepository;
import com.zv.catalogo_service.events.PedidoCreadoEvent;
import com.zv.catalogo_service.events.StockRechazadoEvent;
import com.zv.catalogo_service.events.StockReservadoEvent;
import com.zv.catalogo_service.saga.CatalogoEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaStockServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CatalogoEventPublisher eventPublisher;

    @InjectMocks
    private ReservaStockService service;

    private UUID productoId;
    private Producto producto;

    @BeforeEach
    void setUp() {
        productoId = UUID.randomUUID();
        producto = new Producto();
        producto.setId(productoId);
        producto.setNombre("Mouse");
        producto.setPrecio(new BigDecimal("50.50"));
        producto.setStock(10);
    }

    private PedidoCreadoEvent pedidoCon(UUID productoId, int cantidad) {
        return new PedidoCreadoEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                productoId,
                cantidad,
                new BigDecimal("101.00"),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("con stock suficiente: descuenta stock y publica StockReservado")
    void reservaExitosa() {
        PedidoCreadoEvent evento = pedidoCon(productoId, 2);
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));

        service.reservar(evento);

        assertThat(producto.getStock()).isEqualTo(8);

        ArgumentCaptor<StockReservadoEvent> captor = ArgumentCaptor.forClass(StockReservadoEvent.class);
        verify(eventPublisher).publicarStockReservado(captor.capture());
        StockReservadoEvent publicado = captor.getValue();
        assertThat(publicado.pedidoId()).isEqualTo(evento.pedidoId());
        assertThat(publicado.clienteId()).isEqualTo(evento.clienteId());
        assertThat(publicado.productoId()).isEqualTo(productoId);
        assertThat(publicado.cantidad()).isEqualTo(2);
        assertThat(publicado.montoTotal()).isEqualByComparingTo("101.00");

        verify(eventPublisher, never()).publicarStockRechazado(any());
    }

    @Test
    @DisplayName("con stock insuficiente: no toca el stock y publica StockRechazado")
    void stockInsuficiente() {
        PedidoCreadoEvent evento = pedidoCon(productoId, 15);
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));

        service.reservar(evento);

        assertThat(producto.getStock()).isEqualTo(10);

        ArgumentCaptor<StockRechazadoEvent> captor = ArgumentCaptor.forClass(StockRechazadoEvent.class);
        verify(eventPublisher).publicarStockRechazado(captor.capture());
        assertThat(captor.getValue().pedidoId()).isEqualTo(evento.pedidoId());
        assertThat(captor.getValue().motivo()).contains("Stock insuficiente");

        verify(eventPublisher, never()).publicarStockReservado(any());
    }

    @Test
    @DisplayName("producto inexistente: publica StockRechazado con motivo")
    void productoNoExiste() {
        UUID inexistente = UUID.randomUUID();
        PedidoCreadoEvent evento = pedidoCon(inexistente, 1);
        when(productoRepository.findById(inexistente)).thenReturn(Optional.empty());

        service.reservar(evento);

        ArgumentCaptor<StockRechazadoEvent> captor = ArgumentCaptor.forClass(StockRechazadoEvent.class);
        verify(eventPublisher).publicarStockRechazado(captor.capture());
        assertThat(captor.getValue().productoId()).isEqualTo(inexistente);
        assertThat(captor.getValue().motivo()).isEqualTo("Producto no existe");

        verify(eventPublisher, never()).publicarStockReservado(any());
    }

    @Test
    @DisplayName("stock exacto: permite reservar todo y deja stock en 0")
    void stockExacto() {
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));

        service.reservar(pedidoCon(productoId, 10));

        assertThat(producto.getStock()).isZero();
        verify(eventPublisher).publicarStockReservado(any());
    }
}