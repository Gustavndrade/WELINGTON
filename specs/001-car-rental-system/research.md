# Research: Sistema de Aluguel de Carros

**Feature**: `001-car-rental-system`
**Phase**: 0 — Outline & Research
**Date**: 2026-09-22

## Decisões de Stack

### Decision 1: Java 21 com Spring Boot 3.x

- **Decision**: Java 21 (LTS) + Spring Boot 3.3.x via Spring Initializr
- **Rationale**: Spring Boot 3 exige Java 17+ e oferece suporte nativo a Jakarta EE 10 (`jakarta.*` em vez de `javax.*`). Java 21 é o LTS mais recente e amplamente suportado.
- **Alternatives considered**: Java 17 (também válido, mas Java 21 é preferível por ser LTS mais recente); Spring Boot 2.x (legado, usa `javax.*` — evitar em novos projetos).
- **Impact on code**: Imports usarão `jakarta.persistence.*`, `jakarta.validation.*`.

---

### Decision 2: Estrutura de pacotes

- **Decision**: Pacote base `com.rental.carros` com subpacotes `controller`, `model`, `service`, `repository`
- **Rationale**: Estrutura canônica Spring Boot alinhada com o requisito de arquitetura em camadas da disciplina.
- **Alternatives considered**: Pacote por feature (não aplicável para um único domínio de entidade).

---

### Decision 3: Entidade `Veiculo` — atributos e validações

- **Decision**: 6 atributos além do `id`:

  | Campo | Tipo Java | Validações |
  |---|---|---|
  | `modelo` | `String` | `@NotBlank`, `@Size(min=2, max=100)` |
  | `marca` | `String` | `@NotBlank`, `@Size(min=2, max=60)` |
  | `ano` | `Integer` | `@NotNull`, `@Min(1900)`, `@Max(2030)` |
  | `placa` | `String` | `@NotBlank`, `@Size(min=7, max=8)` |
  | `valorDiaria` | `BigDecimal` | `@NotNull`, `@DecimalMin("0.01")` |
  | `disponivel` | `Boolean` | `@NotNull` |

- **Rationale**: `BigDecimal` é o tipo correto para valores monetários (sem arredondamento de ponto flutuante). `Boolean` para disponibilidade é simples e direto. Atende ao mínimo de 5 atributos exigidos.
- **Alternatives considered**: `Double` para valorDiaria (rejeitado por imprecisão monetária); `String` para ano (rejeitado por perder semântica numérica e validação `@Min`/`@Max`).

---

### Decision 4: Mapeamento de rotas HTTP

- **Decision**: Convenção RESTful de URLs para operações de formulário HTML (sem API REST):

  | Operação | Método | Rota |
  |---|---|---|
  | Listar veículos | GET | `/veiculos` |
  | Exibir formulário de criação | GET | `/veiculos/novo` |
  | Salvar novo veículo | POST | `/veiculos` |
  | Exibir formulário de edição | GET | `/veiculos/{id}/editar` |
  | Atualizar veículo | POST | `/veiculos/{id}` |
  | Excluir veículo | POST | `/veiculos/{id}/excluir` |

- **Rationale**: HTML forms suportam apenas GET e POST. Para Delete e Update, usa-se POST com rota semântica (padrão Spring MVC com Thymeleaf).
- **Alternatives considered**: `_method` hidden field com `HiddenHttpMethodFilter` para simular PUT/DELETE (evitado por adicionar complexidade desnecessária ao escopo acadêmico).

---

### Decision 5: Configuração PostgreSQL

- **Decision**: Banco local `aluguel_carros` com usuário `postgres`; configuração em `application.properties`; `ddl-auto=update` para criação automática de schema.
- **Rationale**: `update` é ideal para desenvolvimento — cria a tabela na primeira execução e atualiza o schema se a entidade mudar, sem apagar dados.
- **Alternatives considered**: `create-drop` (apaga dados ao reiniciar — inviável para demo); `validate` (exige schema pré-criado — adiciona passo extra desnecessário).

---

### Decision 6: Thymeleaf — padrão de formulário

- **Decision**: Usar `th:object="${veiculo}"`, `th:field="*{campo}"`, `th:action="@{/rota}"` com `th:errors="*{campo}"` para exibição de erros.
- **Rationale**: Requisito explícito da disciplina. `th:errors` é o mecanismo padrão do Thymeleaf para exibir erros de `BindingResult`.
- **Alternatives considered**: Nenhuma — requisito mandatório.

---

### Decision 7: CSS próprio sem framework

- **Decision**: `style.css` com estilos customizados para tabela, formulário, botões e mensagens de erro.
- **Rationale**: Requisito explícito — não usar Bootstrap ou framework CSS. Foco em CSS básico organizado.
- **Alternatives considered**: Bootstrap (explicitamente descartado pela disciplina).

## Conclusão

Nenhum NEEDS CLARIFICATION restante. Stack 100% definido pelos requisitos do trabalho. Prosseguir para Phase 1 (data-model, contracts, quickstart).
