package com.zv.notificaciones_service.service;

import com.zv.notificaciones_service.events.PedidoCanceladoEvent;
import com.zv.notificaciones_service.events.PedidoConfirmadoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacionService {

    public void notificarConfirmacion(PedidoConfirmadoEvent e) {
        enviar(e.clienteId().toString(),
                "Tu pedido fue confirmado",
                "¡Gracias por tu compra! Tu pedido %s por S/ %s fue confirmado y pagado."
                        .formatted(e.pedidoId(), e.montoTotal()));
    }

    public void notificarCancelacion(PedidoCanceladoEvent e) {
        enviar(e.clienteId().toString(),
                "Tu pedido fue cancelado",
                "Lamentamos informarte que tu pedido %s fue cancelado. Motivo: %s."
                        .formatted(e.pedidoId(), e.motivo() != null ? e.motivo() : "no especificado"));
    }

    /** Simula el canal de envío (email/SMS/push). Aquí iría JavaMailSender, Twilio, etc. */
    private void enviar(String destinatario, String asunto, String cuerpo) {
        log.info("""
        
        +--------------- NOTIFICACION ---------------
        | Para:   cliente {}
        | Asunto: {}
        | {}
        +--------------------------------------------""",
                destinatario, asunto, cuerpo);
    }
}