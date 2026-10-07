package com.friodiag.friodiag.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordens_servico")
public class OrdemServico {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String equipamento;

    @Column(nullable = false, length = 2000)
    private String sintomas;

    private String diagnostico;

    @Column(name = "mensagem_erro")
    private String mensagemErro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdem status;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    protected OrdemServico() {}

    public OrdemServico(String equipamento, String sintomas) {
        this.equipamento = equipamento;
        this.sintomas = sintomas;
        status = StatusOrdem.PENDENTE;
        criadoEm = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public StatusOrdem getStatus() {
        return status;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }
}
