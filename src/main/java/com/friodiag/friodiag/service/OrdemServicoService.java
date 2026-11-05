package com.friodiag.friodiag.service;

import com.friodiag.friodiag.dto.CriarOrdemRequest;
import com.friodiag.friodiag.dto.OrdemResponse;
import com.friodiag.friodiag.entity.OrdemServico;
import com.friodiag.friodiag.exception.OrdemNaoEncontradaException;
import com.friodiag.friodiag.messaging.DiagnosticoSolicitadoEvent;
import com.friodiag.friodiag.repository.OrdemServicoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrdemServicoService {
    private final OrdemServicoRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public OrdemServicoService(
            OrdemServicoRepository repository,
            ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrdemResponse criar(CriarOrdemRequest request) {
        OrdemServico os = new OrdemServico(request.equipamento(), request.sintomas());
        OrdemServico salva = repository.save(os);
        eventPublisher.publishEvent(new DiagnosticoSolicitadoEvent(salva.getId()));
        return OrdemResponse.de(salva);
    }

    @Transactional(readOnly = true)
    public OrdemResponse buscarPorId(UUID id) {
        OrdemServico os = repository.findById(id)
                .orElseThrow(() -> new OrdemNaoEncontradaException(id));
        return OrdemResponse.de(os);
    }

    @Transactional(readOnly = true)
    public Page<OrdemResponse> listar(Pageable pageable) {
        return repository.findAll(pageable)
                .map(OrdemResponse::de);
    }
}
