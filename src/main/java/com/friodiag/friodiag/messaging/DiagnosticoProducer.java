package com.friodiag.friodiag.messaging;

import com.friodiag.friodiag.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DiagnosticoProducer {

    private final RabbitTemplate rabbitTemplate;

    public DiagnosticoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void solicitarDiagnostico(UUID ordemId) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                ordemId.toString()
        );
    }
}
