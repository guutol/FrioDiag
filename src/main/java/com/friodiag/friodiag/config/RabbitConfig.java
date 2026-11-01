package com.friodiag.friodiag.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String FILA = "friodiag.diagnosticos";
    public static final String EXCHANGE = "friodiag.exchange";
    public static final String ROUTING_KEY = "diagnostico.solicitado";

    @Bean
    public Queue filaDiagnosticos() {
        return QueueBuilder.durable(FILA).build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue fila, DirectExchange exchange) {
        return BindingBuilder.bind(fila).to(exchange).with(ROUTING_KEY);
    }

}
