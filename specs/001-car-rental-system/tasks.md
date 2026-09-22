# Tasks: Sistema de Aluguel de Carros

**Input**: Design documents from `specs/001-car-rental-system/`

**Prerequisites**: [plan.md](plan.md) âœ… | [spec.md](spec.md) âœ… | [research.md](research.md) âœ… | [data-model.md](data-model.md) âœ… | [contracts/web-routes.md](contracts/web-routes.md) âœ… | [quickstart.md](quickstart.md) âœ…

**Tests**: NÃ£o obrigatÃ³rios neste escopo acadÃªmico â€” validaÃ§Ã£o manual via quickstart.md.

**Organization**: Tarefas agrupadas por User Story para entrega incremental e teste independente.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Pode rodar em paralelo (arquivos diferentes, sem dependÃªncias)
- **[Story]**: User Story correspondente (US1, US2, US3, US4)
- Todos os caminhos sÃ£o relativos Ã  raiz do projeto Spring Boot

---

## Phase 1: Setup (Infraestrutura Compartilhada)

**Purpose**: CriaÃ§Ã£o e configuraÃ§Ã£o do projeto Spring Boot com todas as dependÃªncias obrigatÃ³rias.

- [x] T001 Criar projeto Spring Boot via Spring Initializr com dependÃªncias: Spring Web, Thymeleaf, Spring Data JPA, PostgreSQL Driver, Validation â€” salvar em `pom.xml` e estrutura `src/`
- [x] T002 Configurar `src/main/resources/application.properties` com URL JDBC, usuÃ¡rio, senha, `ddl-auto=update`, `show-sql=true` e `thymeleaf.cache=false`
- [x] T003 [P] Criar estrutura de pacotes: `com.rental.carros.controller`, `com.rental.carros.model`, `com.rental.carros.service`, `com.rental.carros.repository` em `src/main/java/com/rental/carros/`
- [x] T004 [P] Criar arquivo `src/main/resources/static/css/style.css` com reset bÃ¡sico e variÃ¡veis CSS (cores, fontes, espaÃ§amentos)
- [x] T005 Verificar que a aplicaÃ§Ã£o inicia sem erros (`mvn spring-boot:run`) e conecta ao banco PostgreSQL

**Checkpoint**: Projeto sobe em `http://localhost:8080` sem erros de compilaÃ§Ã£o ou conexÃ£o.

---

## Phase 2: Foundational (PrÃ©-requisitos Bloqueantes)

**Purpose**: Entidade `Veiculo` com mapeamento JPA e validaÃ§Ãµes â€” bloqueia todas as User Stories.

**âš ï¸ CRÃTICO**: Nenhuma User Story pode ser iniciada antes desta fase estar completa.

- [x] T006 Criar entidade `src/main/java/com/rental/carros/model/Veiculo.java` com anotaÃ§Ãµes `@Entity`, `@Table(name="veiculo")`, `@Id`, `@GeneratedValue(strategy=IDENTITY)` e campos: `id` (Long), `modelo` (String), `marca` (String), `ano` (Integer), `placa` (String), `valorDiaria` (BigDecimal), `disponivel` (Boolean)
- [x] T007 Adicionar Bean Validation em `src/main/java/com/rental/carros/model/Veiculo.java`: `@NotBlank` + `@Size(min=2,max=100)` em `modelo`; `@NotBlank` + `@Size(min=2,max=60)` em `marca`; `@NotNull` + `@Min(1900)` + `@Max(2030)` em `ano`; `@NotBlank` + `@Size(min=7,max=8)` em `placa`; `@NotNull` + `@DecimalMin("0.01")` em `valorDiaria`; `@NotNull` em `disponivel`; com mensagens customizadas em portuguÃªs
- [x] T008 Criar `src/main/java/com/rental/carros/repository/VeiculoRepository.java` estendendo `JpaRepository<Veiculo, Long>`
- [x] T009 Criar `src/main/java/com/rental/carros/service/VeiculoService.java` com injeÃ§Ã£o de `VeiculoRepository` via `@Autowired` e stubs dos mÃ©todos: `salvar(Veiculo)`, `listarTodos()`, `buscarPorId(Long)`, `excluir(Long)`
- [x] T010 Verificar que a tabela `veiculo` Ã© criada automaticamente no PostgreSQL ao iniciar a aplicaÃ§Ã£o (`\dt` no psql)

**Checkpoint**: Entidade mapeada, repositÃ³rio e service criados, tabela no banco confirmada.

---

## Phase 3: User Story 1 â€” Cadastrar Novo VeÃ­culo (Priority: P1) ðŸŽ¯ MVP

**Goal**: UsuÃ¡rio acessa formulÃ¡rio, preenche dados de um veÃ­culo e salva no banco PostgreSQL. FormulÃ¡rio exibe erros de validaÃ§Ã£o sem perder dados preenchidos.

**Independent Test**: Acessar `http://localhost:8080/veiculos/novo`, preencher o formulÃ¡rio com dados vÃ¡lidos, clicar em Salvar e verificar o veÃ­culo no banco via `SELECT * FROM veiculo;`.

### Implementation for User Story 1

- [x] T011 [US1] Implementar mÃ©todo `salvar(Veiculo veiculo)` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.save(veiculo)` e retornando o `Veiculo` salvo
- [x] T012 [US1] Criar `src/main/java/com/rental/carros/controller/VeiculoController.java` com `@Controller` + `@RequestMapping("/veiculos")` e mÃ©todos: `GET /veiculos/novo` â†’ `novo(Model)` adicionando `new Veiculo()` ao model e retornando view `"formulario"`; `POST /veiculos` â†’ `salvar(@Valid Veiculo, BindingResult, RedirectAttributes)` com lÃ³gica de validaÃ§Ã£o e redirect
- [x] T013 [US1] Criar `src/main/resources/templates/formulario.html` com: `th:object="${veiculo}"`, `th:action="@{/veiculos}"` para criaÃ§Ã£o, campos `th:field="*{modelo}"`, `th:field="*{marca}"`, `th:field="*{ano}"`, `th:field="*{placa}"`, `th:field="*{valorDiaria}"`, `th:field="*{disponivel}"` (checkbox ou select), `th:errors="*{campo}"` para cada campo, e botÃ£o "Salvar"
- [x] T014 [US1] Estilizar `src/main/resources/static/css/style.css` com estilos para: formulÃ¡rio centralizado, labels e inputs empilhados, classe `.campo-erro` para destaque de erros de validaÃ§Ã£o, botÃ£o "Salvar" com hover
- [x] T015 [US1] Validar CenÃ¡rio 1 e CenÃ¡rio 2 do `quickstart.md`: cadastro vÃ¡lido persiste no banco; formulÃ¡rio invÃ¡lido reexibe com erros e preserva valores

**Checkpoint US1**: FormulÃ¡rio de cadastro funcional â€” salva no banco e exibe erros de validaÃ§Ã£o. MVP demonstrÃ¡vel.

---

## Phase 4: User Story 2 â€” Listar VeÃ­culos Cadastrados (Priority: P2)

**Goal**: UsuÃ¡rio acessa `/veiculos` e vÃª tabela HTML com todos os veÃ­culos do banco, com botÃµes de aÃ§Ã£o (Novo, Editar, Excluir) para cada linha.

**Independent Test**: Com ao menos um veÃ­culo no banco, acessar `http://localhost:8080/veiculos` e verificar tabela com colunas Modelo, Marca, Ano, Placa, Valor DiÃ¡ria, DisponÃ­vel, AÃ§Ãµes.

### Implementation for User Story 2

- [x] T016 [US2] Implementar mÃ©todo `listarTodos()` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.findAll()` e retornando `List<Veiculo>`
- [x] T017 [US2] Adicionar mÃ©todo `GET /veiculos` â†’ `listar(Model model)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que chama `service.listarTodos()`, adiciona lista ao model como `"veiculos"` e retorna view `"lista"`
- [x] T018 [US2] Criar `src/main/resources/templates/lista.html` com: `th:each="v : ${veiculos}"` para iterar, tabela com colunas Modelo, Marca, Ano, Placa, Valor DiÃ¡ria, DisponÃ­vel, AÃ§Ãµes; link "Editar" com `th:href="@{/veiculos/{id}/editar(id=${v.id})}"`, formulÃ¡rio "Excluir" com `th:action="@{/veiculos/{id}/excluir(id=${v.id})}"` method POST, link "Novo VeÃ­culo" com `th:href="@{/veiculos/novo}"`, e mensagem `th:if="${#lists.isEmpty(veiculos)}"` para lista vazia
- [x] T019 [US2] Estilizar `src/main/resources/static/css/style.css` com estilos para tabela: `border-collapse`, cabeÃ§alhos destacados, linhas alternadas, botÃµes de aÃ§Ã£o (Editar em azul, Excluir em vermelho)
- [x] T020 [US2] Validar CenÃ¡rio 4 do `quickstart.md`: listagem exibe todos os registros do banco com colunas e botÃµes corretos

**Checkpoint US2**: Listagem funcional â€” exibe todos os veÃ­culos com aÃ§Ãµes de navegaÃ§Ã£o. US1 + US2 = fluxo bÃ¡sico demonstrÃ¡vel.

---

## Phase 5: User Story 3 â€” Editar VeÃ­culo Existente (Priority: P3)

**Goal**: UsuÃ¡rio clica em "Editar" na listagem, o formulÃ¡rio Ã© prÃ©-preenchido com os dados do veÃ­culo, o usuÃ¡rio altera e salva. ValidaÃ§Ã£o funciona igual ao cadastro.

**Independent Test**: Clicar em "Editar" de qualquer veÃ­culo, alterar `valorDiaria` e salvar â€” verificar valor atualizado na listagem.

### Implementation for User Story 3

- [x] T021 [US3] Implementar mÃ©todo `buscarPorId(Long id)` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.findById(id).orElseThrow(() -> new RuntimeException("VeÃ­culo nÃ£o encontrado: " + id))` e retornando `Veiculo`
- [x] T022 [US3] Adicionar mÃ©todo `GET /veiculos/{id}/editar` â†’ `editar(@PathVariable Long id, Model model)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que chama `service.buscarPorId(id)`, adiciona ao model e retorna view `"formulario"`
- [x] T023 [US3] Adicionar mÃ©todo `POST /veiculos/{id}` â†’ `atualizar(@PathVariable Long id, @Valid Veiculo veiculo, BindingResult result, RedirectAttributes attrs)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que seta o `id` no veiculo, valida, chama `service.salvar(veiculo)` e faz redirect
- [x] T024 [US3] Atualizar `src/main/resources/templates/formulario.html` para: exibir `th:action` dinÃ¢mico â€” `@{/veiculos}` quando `veiculo.id` Ã© nulo (criaÃ§Ã£o) e `@{/veiculos/{id}(id=${veiculo.id})}` quando nÃ£o Ã© nulo (ediÃ§Ã£o); campo `id` oculto com `th:if="${veiculo.id != null}"` e `th:value="${veiculo.id}"`; tÃ­tulo dinÃ¢mico "Novo VeÃ­culo" ou "Editar VeÃ­culo"
- [x] T025 [US3] Validar CenÃ¡rios 5 e 7 do `quickstart.md`: ediÃ§Ã£o atualiza o banco; erros no formulÃ¡rio de ediÃ§Ã£o preservam valores

**Checkpoint US3**: EdiÃ§Ã£o funcional â€” formulÃ¡rio reutilizÃ¡vel para criaÃ§Ã£o e ediÃ§Ã£o. US1 + US2 + US3 = CRUD 75% completo.

---

## Phase 6: User Story 4 â€” Excluir VeÃ­culo (Priority: P4)

**Goal**: UsuÃ¡rio clica em "Excluir" na listagem e o registro Ã© removido do banco. Listagem Ã© atualizada imediatamente.

**Independent Test**: Clicar em "Excluir" de qualquer veÃ­culo e verificar que ele desaparece da tabela e do banco de dados.

### Implementation for User Story 4

- [x] T026 [US4] Implementar mÃ©todo `excluir(Long id)` em `src/main/java/com/rental/carros/service/VeiculoService.java` chamando `repository.deleteById(id)`
- [x] T027 [US4] Adicionar mÃ©todo `POST /veiculos/{id}/excluir` â†’ `excluir(@PathVariable Long id, RedirectAttributes attrs)` em `src/main/java/com/rental/carros/controller/VeiculoController.java` que chama `service.excluir(id)` e faz `redirect:/veiculos`
- [x] T028 [US4] Validar CenÃ¡rio 6 do `quickstart.md`: exclusÃ£o remove o registro do banco e da listagem

**Checkpoint US4**: CRUD 100% completo. Sistema totalmente funcional e demonstrÃ¡vel.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Qualidade final, README e validaÃ§Ã£o completa do quickstart.

- [x] T029 [P] Criar/atualizar `README.md` na raiz do projeto com: Nome do aluno, Nome do sistema ("Sistema de Aluguel de Carros"), DescriÃ§Ã£o resumida, Tecnologias utilizadas, Nome do banco (`aluguel_carros`), InstruÃ§Ãµes de execuÃ§Ã£o
- [x] T030 [P] Adicionar `@RequestMapping("/")` no controller (ou um `HomeController`) que redireciona `/` para `/veiculos`, facilitando o acesso inicial
- [x] T031 Revisar e garantir que todas as mensagens de erro de validaÃ§Ã£o estÃ£o em portuguÃªs no `Veiculo.java` (atributo `message` de cada anotaÃ§Ã£o)
- [x] T032 Revisar organizaÃ§Ã£o e formataÃ§Ã£o do cÃ³digo: indentaÃ§Ã£o consistente, sem imports nÃ£o utilizados, nomes de mÃ©todos e variÃ¡veis em camelCase em portuguÃªs
- [x] T033 Executar todos os 7 cenÃ¡rios de validaÃ§Ã£o do `quickstart.md` e confirmar que cada um passa conforme esperado
- [x] T034 [P] Compactar o projeto em ZIP para entrega: incluir `pom.xml`, pasta `src/` completa, `application.properties`, `README.md`

**Checkpoint Final**: Sistema completo, validado e pronto para apresentaÃ§Ã£o.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Sem dependÃªncias â€” iniciar imediatamente
- **Foundational (Phase 2)**: Depende da conclusÃ£o do Setup â€” **BLOQUEIA todas as User Stories**
- **US1 â€” Cadastrar (Phase 3)**: Depende da Foundational â€” **MVP mÃ­nimo**
- **US2 â€” Listar (Phase 4)**: Depende da Foundational; integra com US1 (usa mesma entidade e service)
- **US3 â€” Editar (Phase 5)**: Depende de US1 (reutiliza formulario.html) e US2 (link na lista)
- **US4 â€” Excluir (Phase 6)**: Depende de US2 (botÃ£o na lista)
- **Polish (Phase 7)**: Depende de todas as User Stories estarem completas

### User Story Dependencies

```text
Phase 1: Setup
    â†“
Phase 2: Foundational (Veiculo entity + Repository + Service stubs)
    â†“
Phase 3: US1 Cadastrar (Service.salvar + Controller POST /veiculos + formulario.html)
    â†“
Phase 4: US2 Listar (Service.listarTodos + Controller GET /veiculos + lista.html)
    â†“
Phase 5: US3 Editar (Service.buscarPorId + Controller GET+POST /veiculos/{id}/editar + formulario.html update)
    â†“
Phase 6: US4 Excluir (Service.excluir + Controller POST /veiculos/{id}/excluir)
    â†“
Phase 7: Polish (README, redirect /, mensagens PT-BR, ZIP)
```

### Within Each User Story

- Model antes de Service
- Service antes de Controller
- Controller antes de Template
- Template antes de CSS
- CSS antes de validaÃ§Ã£o manual

### Parallel Opportunities

- T003 e T004 (Phase 1) podem rodar em paralelo
- T029 e T030 (Phase 7) podem rodar em paralelo
- T031 e T034 (Phase 7) podem rodar em paralelo

---

## Parallel Example: Setup Phase

```text
# Executar em paralelo apÃ³s T001 e T002:
Task T003: Criar estrutura de pacotes Java
Task T004: Criar style.css com reset e variÃ¡veis
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1: Setup
2. Completar Phase 2: Foundational (CRÃTICO â€” bloqueia tudo)
3. Completar Phase 3: US1 â€” Cadastrar
4. **PARAR e VALIDAR**: Testar cadastro + validaÃ§Ã£o manualmente
5. Demonstrar MVP: formulÃ¡rio funcionando com persistÃªncia no banco

### Incremental Delivery

1. Setup + Foundational â†’ Base pronta
2. + US1 Cadastrar â†’ **MVP demonstrÃ¡vel** (formulÃ¡rio + persistÃªncia + validaÃ§Ã£o)
3. + US2 Listar â†’ CRUD parcial (Create + Read)
4. + US3 Editar â†’ CRUD quase completo (Create + Read + Update)
5. + US4 Excluir â†’ **CRUD 100%** â€” pronto para apresentaÃ§Ã£o
6. + Polish â†’ Entrega final com README e ZIP

### Solo Developer Strategy (Este Projeto)

Com um Ãºnico desenvolvedor, seguir sequencialmente P1 â†’ P2 â†’ P3 â†’ P4 â†’ Polish, fazendo commit `>>>` ao final de cada task concluÃ­da.

---

## Notes

- [P] = arquivos diferentes, sem dependÃªncias entre si na mesma fase
- [Story] = rastreabilidade da task Ã  User Story do spec.md
- Fazer commit `>>>` ao final de cada task ou grupo lÃ³gico
- Cada User Story deve ser testÃ¡vel de forma independente antes de avanÃ§ar
- Evitar: tasks vagas, conflitos de arquivo, dependÃªncias cruzadas que quebrem independÃªncia
- ReferÃªncia de validaÃ§Ã£o: [quickstart.md](quickstart.md)
- ReferÃªncia de rotas: [contracts/web-routes.md](contracts/web-routes.md)
- ReferÃªncia do modelo: [data-model.md](data-model.md)

