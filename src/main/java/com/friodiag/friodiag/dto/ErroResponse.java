package com.friodiag.friodiag.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ErroResponse(
    int status,
    String mensagem,
    List<String> detalhes,
    OffsetDateTime timestamp
) {}


