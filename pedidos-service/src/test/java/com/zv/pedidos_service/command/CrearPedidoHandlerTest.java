package com.zv.pedidos_service.command;


import com.zv.pedidos_service.domain.EstadoPedido;
import com.zv.pedidos_service.domain.Pedido;
import com.zv.pedidos_service.domain.PedidoResumen;
import com.zv.pedidos_service.domain.repository.PedidoRepository;
import com.zv.pedidos_service.domain.repository.PedidoResumenRepository;
import com.zv.pedidos_service.events.PedidoCreadoEvent;
import com.zv.pedidos_service.saga.PedidoEventPublisher;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearPedidoHandler")
class CrearPedidoHandlerTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private PedidoResumenRepository pedidoResumenRepository;
    @Mock private PedidoEventPublisher eventPublisher;

    @InjectMocks private CrearPedidoHandler handler;

    private CrearPedidoCommand comandoValido(){
        return new CrearPedidoCommand(
                UUID.randomUUID(), UUID.randomUUID(), 2, new BigDecimal("150.50"));
    }

    private Pedido pedidoPersistido(CrearPedidoCommand cmd){
        Pedido p = new Pedido();
        p.setId(UUID.randomUUID());
        p.setClienteId(cmd.clienteId());
        p.setProductoId(cmd.productoId());
        p.setCantidad(cmd.cantidad());
        p.setMontoTotal(cmd.montoTotal());
        p.setEstado(EstadoPedido.CREADO);
        p.setFechaCreacion(LocalDateTime.now());
        return p;
    }

    @Test
    @DisplayName("guarda el pedido y devuelve el id generado")
    void guardaPedidoYDevuelveId(){
        CrearPedidoCommand cmd = comandoValido();
        Pedido persistido = pedidoPersistido(cmd);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(persistido);

        UUID resultado = handler.handle(cmd);

        assertThat(resultado).isEqualTo(persistido.getId());

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(captor.capture());
        assertThat(captor.getValue().getClienteId()).isEqualTo(cmd.clienteId());
        assertThat(captor.getValue().getCantidad()).isEqualTo(2);
    }

    @Test
    @DisplayName("proyecta el read model con los datos del pedido guardado")
    void proyectoReadModel(){
        CrearPedidoCommand cmd = comandoValido();
        Pedido persistido = pedidoPersistido(cmd);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(persistido);

        handler.handle(cmd);

        ArgumentCaptor<PedidoResumen> captor = ArgumentCaptor.forClass(PedidoResumen.class);
        verify(pedidoResumenRepository).save(captor.capture());

        PedidoResumen resumen = captor.getValue();
        assertThat(resumen.getPedidoId()).isEqualTo(persistido.getId());
        assertThat(resumen.getNombreEstado()).isEqualTo("CREADO");
        assertThat(resumen.getMontoTotal()).isEqualByComparingTo("150.50");
    }

    @Test
    @DisplayName("publica PedidoCreadoEvent con el id del pedido")
    void publicaEvento(){
        CrearPedidoCommand cmd = comandoValido();
        Pedido persistido = pedidoPersistido(cmd);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(persistido);

        handler.handle(cmd);

        ArgumentCaptor<PedidoCreadoEvent> captor = ArgumentCaptor.forClass(PedidoCreadoEvent.class);
        verify(eventPublisher).publicarPedidoCreado(captor.capture());

        PedidoCreadoEvent evento = captor.getValue();
        assertThat(evento.pedidoId()).isEqualTo(persistido.getId());
        assertThat(evento.productoId()).isEqualTo(cmd.productoId());
        assertThat(evento.cantidad()).isEqualTo(2);
    }

    @Test
    @DisplayName("no publica el evento si falla el guardado")
    void noPublicaSiFallaGuardado(){
        when(pedidoRepository.save(any(Pedido.class)))
                .thenThrow(new RuntimeException("BD caída"));

        try {
            handler.handle(comandoValido());
        } catch (RuntimeException ignored){ }

        verifyNoInteractions(eventPublisher);
    }
}
