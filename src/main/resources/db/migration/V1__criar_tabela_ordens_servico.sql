CREATE TABLE ordens_servico (
    id UUID primary key,
    equipamento varchar(100) not null,
    sintomas varchar(2000) not null,
    status varchar(20) not null check (status in ('PENDENTE', 'CONCLUIDO', 'ERRO')),
    diagnostico text,
    mensagem_erro text,
    criado_em timestamp with time zone not null
);

CREATE INDEX idx_ordens_servico_criado_em ON ordens_servico (criado_em);
