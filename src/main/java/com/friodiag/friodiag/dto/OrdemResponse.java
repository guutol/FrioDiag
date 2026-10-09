package com.friodiag.friodiag.dto;

import com.friodiag.friodiag.entity.OrdemServico;
import com.friodiag.friodiag.entity.StatusOrdem;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrdemResponse(UUID id, String equipamento, String sintomas, StatusOrdem status, String diagnostico, String mensagemErro, OffsetDateTime criadoEm) {
    public static OrdemResponse de(OrdemServico o) {
        return new OrdemResponse(o.getId(), o.getEquipamento(), o.getSintomas(), o.getStatus(), o.getDiagnostico(), o.getMensagemErro(), o.getCriadoEm());
    }
}
