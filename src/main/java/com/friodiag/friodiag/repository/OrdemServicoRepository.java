package com.friodiag.friodiag.repository;

import com.friodiag.friodiag.entity.OrdemServico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, UUID> {
}
