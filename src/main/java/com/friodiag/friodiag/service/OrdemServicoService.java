package com.friodiag.friodiag.service;

import com.friodiag.friodiag.dto.CriarOrdemRequest;
import com.friodiag.friodiag.dto.OrdemResponse;
import com.friodiag.friodiag.entity.OrdemServico;
import com.friodiag.friodiag.repository.OrdemServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
}
