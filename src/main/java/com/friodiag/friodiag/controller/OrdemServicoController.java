package com.friodiag.friodiag.controller;

import com.friodiag.friodiag.dto.CriarOrdemRequest;
import com.friodiag.friodiag.dto.OrdemResponse;
import com.friodiag.friodiag.service.OrdemServicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {
    private final OrdemServicoService service;

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrdemResponse> criar(
            @RequestBody CriarOrdemRequest request) {

        OrdemResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemResponse> buscarPorId(@PathVariable UUID id) {
        OrdemResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrdemResponse>> listar (
            @PageableDefault(
                    size = 20,
                    sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }
}
