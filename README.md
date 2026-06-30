# ms-pagamento

Microsserviço responsável pelo processamento de pagamentos de renovação de assinaturas.

## Tecnologias

- Java 25 + Spring Boot 3.5.3
- Spring MVC + JPA + PostgreSQL 16
- Spring Kafka (consumidor e produtor)
- Flyway para migrações
- Sealed classes Java 17 (`ResultadoPagamento`)
- Pattern Matching para switch Java 21
- MapStruct + Lombok
- Swagger/OpenAPI 3 (springdoc 2.8.9)
- Micrometer Tracing + Zipkin
- JaCoCo (90% de cobertura)

## Arquitetura

Arquitetura hexagonal:

- `domain/` — modelos com sealed interface `ResultadoPagamento`
- `application/` — caso de uso e DTOs
- `infrastructure/` — adapters Kafka, JPA, simulador de pagamento

## Fluxo de processamento

```
ms-assinatura → [assinatura.renovacao.solicitada] → ms-pagamento
ms-pagamento  → SimuladorPagamento → salva resultado
ms-pagamento  → [pagamento.resultado] → ms-assinatura
```

## Tópicos Kafka

| Tópico                            | Produção | Consumo | Descrição                        |
|-----------------------------------|----------|---------|----------------------------------|
| `assinatura.renovacao.solicitada` |          | X       | Receber solicitação de renovação |
| `pagamento.resultado`             | X        |         | Publicar resultado do pagamento  |

Tópicos DLT (Dead Letter): `assinatura.renovacao.solicitada.DLT`

## Simulador de Pagamento

O serviço usa um simulador configurável:

| Configuração     | Descrição                            | Padrão |
|------------------|--------------------------------------|--------|
| `TAXA_APROVACAO` | Probabilidade de aprovação (0.0-1.0) | 0.8    |

Com `TAXA_APROVACAO=0.8`, 80% dos pagamentos são aprovados. O resultado usa sealed interface Java 17:

- `ResultadoPagamento.Aprovado(UUID pagamentoId)`
- `ResultadoPagamento.Recusado(UUID pagamentoId, String motivo)`

## Idempotência

Pagamentos já processados para uma mesma assinatura são ignorados (verificação por `assinatura_id`).

## Endpoints

| Método | Path               | Descrição    |
|--------|--------------------|--------------|
| GET    | `/actuator/health` | Health check |

> Este serviço é orientado a eventos e não possui endpoints REST de negócio.

## Variáveis de Ambiente

| Variável                  | Descrição                      | Padrão      |
|---------------------------|--------------------------------|-------------|
| `DB_URL`                  | JDBC URL do PostgreSQL         | obrigatório |
| `DB_USERNAME`             | Usuário do banco               | obrigatório |
| `DB_PASSWORD`             | Senha do banco                 | obrigatório |
| `KAFKA_BOOTSTRAP_SERVERS` | Servidores Kafka               | obrigatório |
| `TAXA_APROVACAO`          | Taxa de aprovação do simulador | 0.8         |
| `ZIPKIN_URL`              | URL do Zipkin                  | opcional    |

## Como executar

```bash
# Com toda a infraestrutura via Docker Compose (a partir da raiz)
docker compose up --build

# Apenas a infraestrutura, serviço rodando localmente
cd ..
docker compose up postgres kafka zookeeper -d
cd ms-pagamento
./mvnw spring-boot:run
```

## Documentação da API

Com o serviço em execução:

- Actuator: http://localhost:8083/actuator/health
- Prometheus metrics: http://localhost:8083/actuator/prometheus

## Testes

```bash
./mvnw test      # Executar testes
./mvnw verify    # Testes + verificação de cobertura JaCoCo
```

Cobertura mínima configurada: **90% de linhas**.

---

### Contadores de Métricas

- **Observabilidade:** dashboard Grafana exibe taxa de aprovacao em tempo real.
- **Alertas:** `assinatura.suspensas.total` aumentando alerta problema no gateway de pagamento.
- **SLIs/SLOs:** metrica `assinatura.renovacoes.resultado` permite calcular taxa de sucesso de renovações.
- **Custo zero em codigo:** `counter.increment()` — não afeta a lógica de negócio.

### Dashboard local: Prometheus + Grafana via Docker

O projeto ja expoe o endpoint `/actuator/prometheus` (configurado em `application.yml`
com `include: health,info,metrics,prometheus`).

### Tracing distribuido via headers

O Spring Kafka + Micrometer Tracing propagam o `traceId` automaticamente nos
headers das mensagens Kafka. O mesmo `traceId` atravessa ms-assinatura -> Kafka
-> ms-pagamento -> Kafka -> ms-assinatura. 

Com Micrometer e Zipkin, uma unica tela mostra:
  [HTTP POST /v1/assinaturas]         50ms
    [R2DBC: SELECT usuario_ativo]     10ms
    [HTTP GET ms-usuario/usuarios]    80ms  <- gargalo!
    [R2DBC: INSERT assinaturas]       15ms
    [Kafka PRODUCE renovacao]          5ms
    ...

### Logstash + Elasticsearch (ELK)

Logs centralizados e busca operacional para os três microsservicos.
Com 3 microsservicos rodando juntos, evita o desgaste e perda de dados ao olhar logs por container/pod.

### Sealed Classes + Pattern Matching (Java 17/21)

`ResultadoPagamento` é uma sealed interface com dois casos: `Aprovado` e `Recusado`. O processamento usa pattern matching para switch, eliminando a necessidade de `instanceof` sequenciais e garantindo que o compilador exija cobertura de todos os casos.

### Kafka + Manual ACK + DLQ

O consumidor de `assinatura.renovacao.solicitada` usa `AckMode.MANUAL`: o offset só é confirmado após o pagamento ser salvo e o evento de resultado publicado. Mensagens que falham após as tentativas configuradas vão para `assinatura.renovacao.solicitada.DLT`, evitando perda silenciosa.

### Produtor Kafka Idempotente

O produtor de `pagamento.resultado` tem `enable.idempotence=true`, garantindo que uma mensagem seja publicada exatamente uma vez mesmo em caso de retry por falha de rede. Isso é essencial para evitar que o ms-assinatura processe o mesmo resultado de pagamento duas vezes.

### Idempotência no Banco

Antes de processar qualquer pagamento, o use case verifica se já existe um registro para o `assinatura_id` recebido. Isso torna o consumidor idempotente em relação à re-entrega de mensagens Kafka.

### Arquitetura Hexagonal

O `ProcessarPagamentoUseCaseImpl` não conhece Kafka, JPA ou nenhuma tecnologia de infraestrutura. Ele recebe e retorna tipos de domínio, delegando persistência e publicação para portas de saída (`PagamentoRepositoryPort`, `EventPublisherPort`).

### MapStruct e Flyway

Mapeamento entre DTOs e entidades gerado em tempo de compilação. Migrações SQL versionadas garantem reprodutibilidade do esquema em qualquer ambiente.

