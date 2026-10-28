# consent-query-service

Consultas de consentimento e PIP (Policy Information Point) para as políticas de autorização.

Parte do **Consent & Audit Architecture**, projeto pessoal de Renan Rohers iniciado no TCC sobre controle de acesso e auditabilidade em microsserviços.

[Visão geral e execução local](https://github.com/consent-audit-arch/infra#readme) · [Diagramas e decisões](https://github.com/consent-audit-arch/infra/blob/main/docs/architecture.md) · [Console](https://github.com/consent-audit-arch/consent-frontend)

## Papel na arquitetura

Lê a projeção atualizada pelo command-service. OPA usa as consultas individuais ou em lote como fonte de informação sobre consentimento. Esta leitura fica no caminho da autorização; o dashboard possui uma projeção assíncrona separada.

## Tecnologias

Java 21 · Spring Boot · Maven. Integrações deste componente: PostgreSQL compartilhado com command-service.

## Executar e desenvolver

O [guia central](https://github.com/consent-audit-arch/infra#executar-localmente) configura bancos, mensageria, identidade, imagens e sidecars OPA. No Compose, este serviço fica em `http://localhost:8081`.

Para compilar e executar os testes a partir da raiz deste repositório, instale primeiro o starter no Maven local (os dois repositórios devem estar lado a lado):

```bash
mvn -f ../tcc-security-starter/pom.xml install
mvn -f consent_query_service/pom.xml verify
```

Os testes de integração podem exigir Docker e dependências externas; consulte as configurações em `consent_query_service/src/main/resources`. Para executar o conjunto integrado, use o Compose central.

## API principal

```text
GET /api/v1/consent/{userId}
GET /api/v1/consent/{userId}/authorizations
GET /api/v1/consent/{userId}/history
POST /api/v1/consent/batch/authorizations
```

A autenticação usa JWT do realm `tcc`. Nos endpoints protegidos pelo PEP, o contexto pode incluir `X-Purpose`, `X-Data-Subject-Id`, `X-Data-Subject-Ids`, `X-Data-Categories` e `X-Correlation-Id`, conforme a operação e a policy. A [coleção Postman](https://github.com/consent-audit-arch/infra/blob/main/postman_collection.json) reúne exemplos.

## Escopo

Protótipo para estudo de autorização contextual e rastreabilidade com dados de demonstração. A cobertura de políticas varia por serviço. Os [limites e próximos passos](https://github.com/consent-audit-arch/infra/blob/main/docs/architecture.md#limites-e-evolução) descrevem as garantias e os pontos ainda em evolução.
