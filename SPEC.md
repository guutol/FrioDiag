# FrioDiag — Especificação

API onde um técnico de refrigeração registra uma ordem de serviço (OS) com o equipamento e os
sintomas, e recebe um diagnóstico sugerido gerado por IA local (possíveis causas, testes
recomendados, componentes a verificar e cuidados de segurança).

O diagnóstico é processado de forma **assíncrona**: a API salva a OS, responde na hora e a IA
trabalha em segundo plano. O técnico consulta o resultado depois.

## Fluxo

1. O técnico envia `POST /ordens` com equipamento e sintomas.
2. A API valida, salva a OS com status `PENDENTE` e publica o id da OS no RabbitMQ.
3. A API responde `202 Accepted` com a OS criada.
4. Um consumer lê a mensagem, busca a OS e pede o diagnóstico à IA (Spring AI + Ollama).
5. Em caso de sucesso, a OS vai para `CONCLUIDO` com o diagnóstico. Em falha definitiva, vai
   para `ERRO` com a mensagem de erro.
6. O técnico consulta `GET /ordens/{id}`.

## Ordem de serviço

| Campo          | Tipo           | Obrigatório | Regras                                          |
|----------------|----------------|-------------|-------------------------------------------------|
| `id`           | UUID           | sim         | Gerado pela API.                                |
| `equipamento`  | texto          | sim         | 1 a 100 caracteres, não pode ser só espaços.    |
| `sintomas`     | texto          | sim         | 10 a 2000 caracteres.                           |
| `status`       | enum           | sim         | `PENDENTE`, `CONCLUIDO` ou `ERRO`.              |
| `diagnostico`  | texto          | não         | Preenchido só quando `CONCLUIDO`.               |
| `mensagemErro` | texto          | não         | Preenchido só quando `ERRO`.                    |
| `criadoEm`     | data/hora      | sim         | Gerado pela API, com fuso horário.              |

O cliente só envia `equipamento` e `sintomas`. Os demais campos são controlados pela API.

## Status

| Status      | Significado                                  | Quando muda                                   |
|-------------|----------------------------------------------|-----------------------------------------------|
| `PENDENTE`  | OS registrada, diagnóstico ainda não feito.  | Ao ser criada no POST.                        |
| `CONCLUIDO` | Diagnóstico gerado e salvo.                  | Quando o consumer termina com sucesso.        |
| `ERRO`      | Não foi possível gerar o diagnóstico.        | Quando todas as tentativas falharam.          |

Transições permitidas: `PENDENTE -> CONCLUIDO` e `PENDENTE -> ERRO`. `CONCLUIDO` e `ERRO` são
finais.

## Endpoints

### `POST /ordens`

Cria uma OS.

Request:

```json
{
  "equipamento": "Geladeira Brastemp Frost Free BRM44",
  "sintomas": "Freezer gelando normal, mas a parte de baixo não gela. Ventilador interno não gira."
}
```

Response `202 Accepted` (na Etapa 1, antes do RabbitMQ, responde `201 Created`):

```json
{
  "id": "3f1c2a9e-7b4d-4c1a-9f0e-2d5b8a6c1e47",
  "equipamento": "Geladeira Brastemp Frost Free BRM44",
  "sintomas": "Freezer gelando normal, mas a parte de baixo não gela. Ventilador interno não gira.",
  "status": "PENDENTE",
  "diagnostico": null,
  "mensagemErro": null,
  "criadoEm": "2026-10-06T14:30:00-03:00"
}
```

Erros: `400` (dados inválidos ou JSON malformado), `503` (limite diário atingido).

### `GET /ordens/{id}`

Busca uma OS pelo id.

- `200 OK` com a OS, no mesmo formato da resposta do POST.
- `404 Not Found` se a OS não existir.

### `GET /ordens`

Lista as OS, paginado e da mais recente para a mais antiga.

- Parâmetros opcionais: `page` (padrão 0) e `size` (padrão 20).
- `200 OK` com a lista.

## Formato de erro

Todos os erros da API seguem o mesmo formato:

```json
{
  "status": 400,
  "mensagem": "Dados inválidos",
  "detalhes": ["sintomas: sintomas devem ter entre 10 e 2000 caracteres"],
  "timestamp": "2026-10-06T14:30:00-03:00"
}
```

Erros inesperados retornam `500` com mensagem genérica, sem detalhes internos (stack trace vai
só para o log).

## Limite diário

- Configurável por `friodiag.limite-diario` (padrão 20).
- Conta as OS criadas no dia corrente, no fuso `America/Sao_Paulo` (o dia começa às 00:00 de
  Brasília).
- Ao atingir o limite, novos `POST /ordens` retornam `503 Service Unavailable` e a OS não é
  salva.
- Os `GET` continuam funcionando normalmente.
- Objetivo: proteger a capacidade da máquina local e, com uma IA paga, o custo.
- Limitação conhecida: duas requisições simultâneas podem passar juntas e ultrapassar o limite
  em 1. Aceitável para o MVP.

## Fora do escopo do MVP

Frontend, autenticação, Redis, Kafka, Kubernetes, microsserviços, Elasticsearch, API Gateway e
cloud.
