package com.friodiag.friodiag.service;

import com.friodiag.friodiag.dto.CriarOrdemRequest;
import com.friodiag.friodiag.dto.OrdemResponse;
import com.friodiag.friodiag.entity.OrdemServico;
import com.friodiag.friodiag.repository.OrdemServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrdemServicoService {
    private final OrdemServicoRepository repository;

    public OrdemServicoService(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public OrdemResponse criar(CriarOrdemRequest request) {
        OrdemServico os = new OrdemServico(request.equipamento(), request.sintomas());
        OrdemServico salva = repository.save(os);
        return OrdemResponse.de(salva);
    }

    public OrdemResponse buscarPorId(UUID id) {
        OrdemServico os = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ordem de seriço não encontrada"));
        return OrdemResponse.de(os);
    }

    public Page<OrdemResponse> listar(Pageable pageable) {
        return repository.findAll(pageable)
                .map(OrdemResponse::de);
    }
}
