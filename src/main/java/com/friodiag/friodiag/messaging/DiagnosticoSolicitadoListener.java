package com.friodiag.friodiag.messaging;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DiagnosticoSolicitadoListener {

    private final DiagnosticoProducer producer;

    public DiagnosticoSolicitadoListener(DiagnosticoProducer producer) {
        this.producer = producer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoSolicitarDiagnostico(DiagnosticoSolicitadoEvent evento) {
        producer.solicitarDiagnostico(evento.ordemId());
    }
}
