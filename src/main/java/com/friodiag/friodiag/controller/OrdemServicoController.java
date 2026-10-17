package com.friodiag.friodiag.controller;

import com.friodiag.friodiag.dto.CriarOrdemRequest;
import com.friodiag.friodiag.dto.OrdemResponse;
import com.friodiag.friodiag.service.OrdemServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
