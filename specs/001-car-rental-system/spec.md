# Feature Specification: Sistema de Aluguel de Carros

**Feature Branch**: `001-car-rental-system`

**Created**: 2026-09-22

**Status**: Draft

**Input**: User description: "Sistema de aluguel de carros simples usando Spring Boot, Spring MVC, Thymeleaf, Bean Validation, Spring Data JPA e PostgreSQL, com CRUD completo da entidade Veiculo (carro)."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Cadastrar Novo Veículo (Priority: P1)

O administrador acessa o formulário de cadastro, preenche os dados do veículo (modelo, marca, ano, placa, valor diária, disponibilidade) e submete. O sistema valida os dados, salva no banco e exibe mensagem de sucesso redirecionando para a listagem.

**Why this priority**: É o núcleo do sistema. Sem o cadastro funcional, nenhuma outra operação faz sentido. Entrega valor imediato ao demonstrar persistência.

**Independent Test**: Pode ser testado abrindo o formulário em `/veiculos/novo`, preenchendo todos os campos corretamente e verificando que o veículo aparece na listagem após o submit.

**Acceptance Scenarios**:

1. **Given** o formulário de cadastro está aberto, **When** o usuário preenche todos os campos obrigatórios corretamente e clica em "Salvar", **Then** o veículo é persistido no banco PostgreSQL e o usuário é redirecionado para a listagem com o novo veículo visível.
2. **Given** o formulário de cadastro está aberto, **When** o usuário deixa campos obrigatórios em branco ou com dados inválidos e clica em "Salvar", **Then** o formulário é reexibido com mensagens de erro específicas por campo e os valores preenchidos são preservados.
3. **Given** o veículo já foi cadastrado, **When** o administrador tenta cadastrar outro com a mesma placa, **Then** o sistema exibe mensagem de erro informando que a placa já está em uso.

---

### User Story 2 - Listar Veículos Cadastrados (Priority: P2)

O administrador acessa a página inicial do sistema e visualiza uma tabela com todos os veículos cadastrados, contendo as principais informações e botões de ação (editar, excluir) para cada registro.

**Why this priority**: Fundamental para navegação e verificação dos dados persistidos. Complementa o cadastro e habilita as demais operações.

**Independent Test**: Pode ser testado acessando `/veiculos` e verificando se a tabela exibe todos os registros do banco com as colunas e botões esperados.

**Acceptance Scenarios**:

1. **Given** existem veículos cadastrados no banco, **When** o usuário acessa a página de listagem, **Then** todos os veículos são exibidos em tabela com colunas: Modelo, Marca, Ano, Placa, Valor Diária, Disponibilidade, Ações.
2. **Given** nenhum veículo está cadastrado, **When** o usuário acessa a página de listagem, **Then** é exibida uma mensagem informativa "Nenhum veículo cadastrado" e um botão para adicionar o primeiro.

---

### User Story 3 - Editar Veículo Existente (Priority: P3)

O administrador clica no botão "Editar" de um veículo da listagem, o formulário é preenchido automaticamente com os dados atuais, o usuário faz as alterações desejadas e salva. O sistema valida, persiste a alteração e redireciona para a listagem.

**Why this priority**: Permite corrigir dados cadastrais sem necessidade de excluir e recadastrar.

**Independent Test**: Pode ser testado clicando em "Editar" de qualquer veículo na listagem, alterando um campo e verificando que a alteração é refletida na tabela.

**Acceptance Scenarios**:

1. **Given** um veículo existe na listagem, **When** o usuário clica em "Editar", **Then** o formulário é exibido pré-preenchido com os dados atuais do veículo.
2. **Given** o formulário de edição está aberto com dados válidos, **When** o usuário altera o valor da diária e clica em "Salvar", **Then** o veículo é atualizado no banco e a listagem reflete o novo valor.
3. **Given** o formulário de edição está aberto, **When** o usuário apaga um campo obrigatório e tenta salvar, **Then** o formulário é reexibido com a mensagem de erro correspondente.

---

### User Story 4 - Excluir Veículo (Priority: P4)

O administrador clica no botão "Excluir" de um veículo na listagem. O sistema remove o registro do banco e redireciona para a listagem atualizada.

**Why this priority**: Completa o CRUD; necessário para manutenção do catálogo (remover veículos inativos, vendidos etc.).

**Independent Test**: Pode ser testado clicando em "Excluir" de qualquer veículo e verificando que ele desaparece da listagem.

**Acceptance Scenarios**:

1. **Given** um veículo existe na listagem, **When** o usuário clica em "Excluir", **Then** o registro é removido do banco e a listagem é atualizada sem o veículo excluído.
2. **Given** a listagem possui apenas um veículo, **When** o usuário exclui esse veículo, **Then** a listagem é exibida com a mensagem "Nenhum veículo cadastrado".

---

### Edge Cases

- O que acontece quando o usuário tenta acessar a edição de um id inexistente? → O sistema exibe mensagem de erro "Veículo não encontrado" ou redireciona para a listagem.
- O que acontece quando o banco de dados está indisponível? → O sistema exibe uma página de erro genérica sem expor detalhes técnicos.
- O que acontece quando o usuário submete o formulário com o campo `ano` fora do intervalo válido (ex: ano futuro)? → O sistema exibe mensagem de validação específica.
- O que acontece quando o campo `valorDiaria` recebe um valor negativo ou zero? → O sistema exibe mensagem de validação impedindo o cadastro.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema DEVE permitir o cadastro de veículos com os atributos: modelo, marca, ano, placa, valorDiaria e disponibilidade.
- **FR-002**: O sistema DEVE validar os campos do formulário usando Bean Validation com no mínimo 3 anotações distintas (`@NotBlank`, `@NotNull`, `@Min`/`@Max`, `@Size`).
- **FR-003**: O sistema DEVE exibir as mensagens de erro de validação diretamente no formulário, preservando os valores já preenchidos pelo usuário.
- **FR-004**: O sistema DEVE persistir todos os dados de veículos em banco de dados PostgreSQL.
- **FR-005**: O sistema DEVE listar todos os veículos cadastrados em tabela HTML na página principal.
- **FR-006**: O sistema DEVE permitir a edição de qualquer veículo cadastrado, pré-preenchendo o formulário com os dados atuais.
- **FR-007**: O sistema DEVE permitir a exclusão de qualquer veículo cadastrado.
- **FR-008**: O sistema DEVE seguir arquitetura em camadas: Controller → Service → Repository → Banco de Dados.
- **FR-009**: O formulário DEVE utilizar as diretivas Thymeleaf `th:object`, `th:field` e `th:action`.
- **FR-010**: O sistema DEVE possuir um arquivo CSS próprio aplicado nas páginas de listagem e formulário.
- **FR-011**: A camada Service DEVE expor no mínimo os métodos: `salvar()`, `listarTodos()`, `buscarPorId()` e `excluir()`.
- **FR-012**: O Repository DEVE estender `JpaRepository` do Spring Data JPA.

### Key Entities *(include if feature involves data)*

- **Veiculo**: Representa um carro disponível para aluguel no sistema. Atributos: `id` (identificador único gerado automaticamente), `modelo` (nome do modelo, ex: "Civic"), `marca` (fabricante, ex: "Honda"), `ano` (ano de fabricação), `placa` (identificação única do veículo), `valorDiaria` (preço por dia de locação em R$), `disponivel` (indica se o veículo está disponível para aluguel).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: O usuário consegue cadastrar um veículo do zero até a confirmação de persistência em menos de 2 minutos.
- **SC-002**: Todos os campos obrigatórios inválidos exibem mensagem de erro específica no mesmo formulário, sem perder os demais dados digitados.
- **SC-003**: A listagem de veículos exibe 100% dos registros presentes no banco de dados, sem omissões.
- **SC-004**: As operações de edição e exclusão refletem imediatamente na listagem após o redirecionamento (sem necessidade de atualização manual da página).
- **SC-005**: O código está organizado nas camadas Controller, Service e Repository, com cada classe responsável apenas pelo seu domínio — verificável durante a apresentação.
- **SC-006**: 100% das tentativas de salvar dados inválidos são rejeitadas com feedback visual ao usuário, sem registros corrompidos no banco.

## Assumptions

- O sistema é destinado a uso acadêmico/demonstrativo e será operado por um único usuário (professor/aluno), sem necessidade de controle de acesso ou autenticação.
- O banco de dados PostgreSQL estará disponível localmente ou via Docker na porta padrão 5432 com configuração no `application.properties`.
- Relacionamentos entre entidades (`@OneToMany`, `@ManyToOne`) estão fora do escopo deste trabalho, conforme orientação da disciplina.
- Paginação, consultas personalizadas, API REST e Spring Security também estão fora do escopo.
- JavaScript no front-end não é obrigatório; a interface funcionará apenas com HTML/Thymeleaf e CSS.
- O Maven será o gerenciador de dependências, com `pom.xml` configurado com as dependências Spring Boot necessárias.
- A propriedade `spring.jpa.hibernate.ddl-auto=update` será usada para criação/atualização automática do schema no banco.
- O sistema será empacotado e entregue como arquivo ZIP contendo o projeto completo com README.md.
