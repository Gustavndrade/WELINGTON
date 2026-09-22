# Tasks: Sistema de Aluguel de Carros

**Input**: Design documents from `specs/001-car-rental-system/`

**Prerequisites**: [plan.md](plan.md) ✅ | [spec.md](spec.md) ✅ | [research.md](research.md) ✅ | [data-model.md](data-model.md) ✅ | [contracts/web-routes.md](contracts/web-routes.md) ✅ | [quickstart.md](quickstart.md) ✅

**Tests**: Não obrigatórios neste escopo acadêmico — validação manual via quickstart.md.

**Organization**: Tarefas agrupadas por User Story para entrega incremental e teste independente.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Pode rodar em paralelo (arquivos diferentes, sem dependências)
- **[Story]**: User Story correspondente (US1, US2, US3, US4)
- Todos os caminhos são relativos à raiz do projeto Spring Boot

---

## Phase 1: Setup (Infraestrutura Compartilhada)

**Purpose**: Criação e configuração do projeto Spring Boot com todas as dependências obrigatórias.

- [ ] T001 Criar projeto Spring Boot via Spring Initializr com dependências: Spring Web, Thymeleaf, Spring Data JPA, PostgreSQL Driver, Validation — salvar em `pom.xml` e estrutura `src/`
- [ ] T002 Configurar `src/main/resources/application.properties` com URL JDBC, usuário, senha, `ddl-auto=update`, `show-sql=true` e `thymeleaf.cache=false`
- [ ] T003 [P] Criar estrutura de pacotes: `com.rental.carros.controller`, `com.rental.carros.model`, `com.rental.carros.service`, `com.rental.carros.repository` em `src/main/java/com/rental/carros/`
- [ ] T004 [P] Criar arquivo `src/main/resources/static/css/style.css` com reset básico e variáveis CSS (cores, fontes, espaçamentos)
- [ ] T005 Verificar que a aplicação inicia sem erros (`mvn spring-boot:run`) e conecta ao banco PostgreSQL

**Checkpoint**: Projeto sobe em `http://localhost:8080` sem erros de compilação ou conexão.

---

## Phase 2: Foundational (Pré-requisitos Bloqueantes)

**Purpose**: Entidade `Veiculo` com mapeamento JPA e validações — bloqueia todas as User Stories.

**⚠️ CRÍTICO**: Nenhuma User Story pode ser iniciada antes desta fase estar completa.

- [ ] T006 Criar entidade `src/main/java/com/rental/carros/model/Veiculo.java` com anotações `@Entity`, `@Table(name="veiculo")`, `@Id`, `@GeneratedValue(strategy=IDENTITY)` e campos: `id` (Long), `modelo` (String), `marca` (String), `ano` (Integer), `placa` (String), `valorDiaria` (BigDecimal), `disponivel` (Boolean)
- [ ] T007 Adicionar Bean Validation em `src/main/java/com/rental/carros/model/Veiculo.java`: `@NotBlank` + `@Size(min=2,max=100)` em `modelo`; `@NotBlank` + `@Size(min=2,max=60)` em `marca`; `@NotNull` + `@Min(1900)` + `@Max(2030)` em `ano`; `@NotBlank` + `@Size(min=7,max=8)` em `placa`; `@NotNull` + `@DecimalMin("0.01")` em `valorDiaria`; `@NotNull` em `disponivel`; com mensagens customizadas em português
- [ ] T008 Criar `src/main/java/com/rental/carros/repository/VeiculoRepository.java` estendendo `JpaRepository<Veiculo, Long>`
- [ ] T009 Criar `src/main/java/com/rental/carros/service/VeiculoService.java` com injeção de `VeiculoRepository` via `@Autowired` e stubs dos métodos: `salvar(Veiculo)`, `listarTodos()`, `buscarPorId(Long)`, `excluir(Long)`
- [ ] T010 Verificar que a tabela `veiculo` é criada automaticamente no PostgreSQL ao iniciar a aplicação (`\dt` no psql)

**Checkpoint**: Entidade mapeada, repositório e service criados, tabela no banco confirmada.

---

## Phase 3: User Story 1 — Cadastrar Novo Veículo (Priority: P1) 🎯 MVP

**Goal**: Usuário acessa formulário, preenche dados de um veículo e salva no banco PostgreSQL. Formulário exibe erros de validação sem perder dados preenchidos.

**Independent Test**: Acessar `http://localhost:8080/veiculos/novo`, preencher o formulário com dados válidos, clicar em Salvar e verificar o veículo no banco via `SELECT * FROM veiculo;`.

### Implementation for User Story 1

- [ ] T011 [US1] Implementar método `salvar(Veiculo veiculo)` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.save(veiculo)` e retornando o `Veiculo` salvo
- [ ] T012 [US1] Criar `src/main/java/com/rental/carros/controller/VeiculoController.java` com `@Controller` + `@RequestMapping("/veiculos")` e métodos: `GET /veiculos/novo` → `novo(Model)` adicionando `new Veiculo()` ao model e retornando view `"formulario"`; `POST /veiculos` → `salvar(@Valid Veiculo, BindingResult, RedirectAttributes)` com lógica de validação e redirect
- [ ] T013 [US1] Criar `src/main/resources/templates/formulario.html` com: `th:object="${veiculo}"`, `th:action="@{/veiculos}"` para criação, campos `th:field="*{modelo}"`, `th:field="*{marca}"`, `th:field="*{ano}"`, `th:field="*{placa}"`, `th:field="*{valorDiaria}"`, `th:field="*{disponivel}"` (checkbox ou select), `th:errors="*{campo}"` para cada campo, e botão "Salvar"
- [ ] T014 [US1] Estilizar `src/main/resources/static/css/style.css` com estilos para: formulário centralizado, labels e inputs empilhados, classe `.campo-erro` para destaque de erros de validação, botão "Salvar" com hover
- [ ] T015 [US1] Validar Cenário 1 e Cenário 2 do `quickstart.md`: cadastro válido persiste no banco; formulário inválido reexibe com erros e preserva valores

**Checkpoint US1**: Formulário de cadastro funcional — salva no banco e exibe erros de validação. MVP demonstrável.

---

## Phase 4: User Story 2 — Listar Veículos Cadastrados (Priority: P2)

**Goal**: Usuário acessa `/veiculos` e vê tabela HTML com todos os veículos do banco, com botões de ação (Novo, Editar, Excluir) para cada linha.

**Independent Test**: Com ao menos um veículo no banco, acessar `http://localhost:8080/veiculos` e verificar tabela com colunas Modelo, Marca, Ano, Placa, Valor Diária, Disponível, Ações.

### Implementation for User Story 2

- [ ] T016 [US2] Implementar método `listarTodos()` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.findAll()` e retornando `List<Veiculo>`
- [ ] T017 [US2] Adicionar método `GET /veiculos` → `listar(Model model)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que chama `service.listarTodos()`, adiciona lista ao model como `"veiculos"` e retorna view `"lista"`
- [ ] T018 [US2] Criar `src/main/resources/templates/lista.html` com: `th:each="v : ${veiculos}"` para iterar, tabela com colunas Modelo, Marca, Ano, Placa, Valor Diária, Disponível, Ações; link "Editar" com `th:href="@{/veiculos/{id}/editar(id=${v.id})}"`, formulário "Excluir" com `th:action="@{/veiculos/{id}/excluir(id=${v.id})}"` method POST, link "Novo Veículo" com `th:href="@{/veiculos/novo}"`, e mensagem `th:if="${#lists.isEmpty(veiculos)}"` para lista vazia
- [ ] T019 [US2] Estilizar `src/main/resources/static/css/style.css` com estilos para tabela: `border-collapse`, cabeçalhos destacados, linhas alternadas, botões de ação (Editar em azul, Excluir em vermelho)
- [ ] T020 [US2] Validar Cenário 4 do `quickstart.md`: listagem exibe todos os registros do banco com colunas e botões corretos

**Checkpoint US2**: Listagem funcional — exibe todos os veículos com ações de navegação. US1 + US2 = fluxo básico demonstrável.

---

## Phase 5: User Story 3 — Editar Veículo Existente (Priority: P3)

**Goal**: Usuário clica em "Editar" na listagem, o formulário é pré-preenchido com os dados do veículo, o usuário altera e salva. Validação funciona igual ao cadastro.

**Independent Test**: Clicar em "Editar" de qualquer veículo, alterar `valorDiaria` e salvar — verificar valor atualizado na listagem.

### Implementation for User Story 3

- [ ] T021 [US3] Implementar método `buscarPorId(Long id)` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.findById(id).orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + id))` e retornando `Veiculo`
- [ ] T022 [US3] Adicionar método `GET /veiculos/{id}/editar` → `editar(@PathVariable Long id, Model model)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que chama `service.buscarPorId(id)`, adiciona ao model e retorna view `"formulario"`
- [ ] T023 [US3] Adicionar método `POST /veiculos/{id}` → `atualizar(@PathVariable Long id, @Valid Veiculo veiculo, BindingResult result, RedirectAttributes attrs)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que seta o `id` no veiculo, valida, chama `service.salvar(veiculo)` e faz redirect
- [ ] T024 [US3] Atualizar `src/main/resources/templates/formulario.html` para: exibir `th:action` dinâmico — `@{/veiculos}` quando `veiculo.id` é nulo (criação) e `@{/veiculos/{id}(id=${veiculo.id})}` quando não é nulo (edição); campo `id` oculto com `th:if="${veiculo.id != null}"` e `th:value="${veiculo.id}"`; título dinâmico "Novo Veículo" ou "Editar Veículo"
- [ ] T025 [US3] Validar Cenários 5 e 7 do `quickstart.md`: edição atualiza o banco; erros no formulário de edição preservam valores

**Checkpoint US3**: Edição funcional — formulário reutilizável para criação e edição. US1 + US2 + US3 = CRUD 75% completo.

---

## Phase 6: User Story 4 — Excluir Veículo (Priority: P4)

**Goal**: Usuário clica em "Excluir" na listagem e o registro é removido do banco. Listagem é atualizada imediatamente.

**Independent Test**: Clicar em "Excluir" de qualquer veículo e verificar que ele desaparece da tabela e do banco de dados.

### Implementation for User Story 4

- [ ] T026 [US4] Implementar método `excluir(Long id)` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.deleteById(id)`
- [ ] T027 [US4] Adicionar método `POST /veiculos/{id}/excluir` → `excluir(@PathVariable Long id, RedirectAttributes attrs)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que chama `service.excluir(id)` e faz `redirect:/veiculos`
- [ ] T028 [US4] Validar Cenário 6 do `quickstart.md`: exclusão remove o registro do banco e da listagem

**Checkpoint US4**: CRUD 100% completo. Sistema totalmente funcional e demonstrável.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Qualidade final, README e validação completa do quickstart.

- [ ] T029 [P] Criar/atualizar `README.md` na raiz do projeto com: Nome do aluno, Nome do sistema ("Sistema de Aluguel de Carros"), Descrição resumida, Tecnologias utilizadas, Nome do banco (`aluguel_carros`), Instruções de execução
- [ ] T030 [P] Adicionar `@RequestMapping("/")` no controller (ou um `HomeController`) que redireciona `/` para `/veiculos`, facilitando o acesso inicial
- [ ] T031 Revisar e garantir que todas as mensagens de erro de validação estão em português no `Veiculo.java` (atributo `message` de cada anotação)
- [ ] T032 Revisar organização e formatação do código: indentação consistente, sem imports não utilizados, nomes de métodos e variáveis em camelCase em português
- [ ] T033 Executar todos os 7 cenários de validação do `quickstart.md` e confirmar que cada um passa conforme esperado
- [ ] T034 [P] Compactar o projeto em ZIP para entrega: incluir `pom.xml`, pasta `src/` completa, `application.properties`, `README.md`

**Checkpoint Final**: Sistema completo, validado e pronto para apresentação.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Sem dependências — iniciar imediatamente
- **Foundational (Phase 2)**: Depende da conclusão do Setup — **BLOQUEIA todas as User Stories**
- **US1 — Cadastrar (Phase 3)**: Depende da Foundational — **MVP mínimo**
- **US2 — Listar (Phase 4)**: Depende da Foundational; integra com US1 (usa mesma entidade e service)
- **US3 — Editar (Phase 5)**: Depende de US1 (reutiliza formulario.html) e US2 (link na lista)
- **US4 — Excluir (Phase 6)**: Depende de US2 (botão na lista)
- **Polish (Phase 7)**: Depende de todas as User Stories estarem completas

### User Story Dependencies

```text
Phase 1: Setup
    ↓
Phase 2: Foundational (Veiculo entity + Repository + Service stubs)
    ↓
Phase 3: US1 Cadastrar (Service.salvar + Controller POST /veiculos + formulario.html)
    ↓
Phase 4: US2 Listar (Service.listarTodos + Controller GET /veiculos + lista.html)
    ↓
Phase 5: US3 Editar (Service.buscarPorId + Controller GET+POST /veiculos/{id}/editar + formulario.html update)
    ↓
Phase 6: US4 Excluir (Service.excluir + Controller POST /veiculos/{id}/excluir)
    ↓
Phase 7: Polish (README, redirect /, mensagens PT-BR, ZIP)
```

### Within Each User Story

- Model antes de Service
- Service antes de Controller
- Controller antes de Template
- Template antes de CSS
- CSS antes de validação manual

### Parallel Opportunities

- T003 e T004 (Phase 1) podem rodar em paralelo
- T029 e T030 (Phase 7) podem rodar em paralelo
- T031 e T034 (Phase 7) podem rodar em paralelo

---

## Parallel Example: Setup Phase

```text
# Executar em paralelo após T001 e T002:
Task T003: Criar estrutura de pacotes Java
Task T004: Criar style.css com reset e variáveis
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1: Setup
2. Completar Phase 2: Foundational (CRÍTICO — bloqueia tudo)
3. Completar Phase 3: US1 — Cadastrar
4. **PARAR e VALIDAR**: Testar cadastro + validação manualmente
5. Demonstrar MVP: formulário funcionando com persistência no banco

### Incremental Delivery

1. Setup + Foundational → Base pronta
2. + US1 Cadastrar → **MVP demonstrável** (formulário + persistência + validação)
3. + US2 Listar → CRUD parcial (Create + Read)
4. + US3 Editar → CRUD quase completo (Create + Read + Update)
5. + US4 Excluir → **CRUD 100%** — pronto para apresentação
6. + Polish → Entrega final com README e ZIP

### Solo Developer Strategy (Este Projeto)

Com um único desenvolvedor, seguir sequencialmente P1 → P2 → P3 → P4 → Polish, fazendo commit `>>>` ao final de cada task concluída.

---

## Notes

- [P] = arquivos diferentes, sem dependências entre si na mesma fase
- [Story] = rastreabilidade da task à User Story do spec.md
- Fazer commit `>>>` ao final de cada task ou grupo lógico
- Cada User Story deve ser testável de forma independente antes de avançar
- Evitar: tasks vagas, conflitos de arquivo, dependências cruzadas que quebrem independência
- Referência de validação: [quickstart.md](quickstart.md)
- Referência de rotas: [contracts/web-routes.md](contracts/web-routes.md)
- Referência do modelo: [data-model.md](data-model.md)
