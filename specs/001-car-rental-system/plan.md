# Implementation Plan: Sistema de Aluguel de Carros

**Branch**: `main` | **Date**: 2026-09-22 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/001-car-rental-system/spec.md`

## Summary

Desenvolver uma aplicação Web de CRUD completo para gerenciamento de veículos de uma locadora, utilizando Java 21 + Spring Boot 3, Spring MVC com Thymeleaf como template engine, Bean Validation para validação de formulários, Spring Data JPA + Hibernate para persistência e PostgreSQL como banco de dados relacional. A arquitetura segue o padrão em camadas Controller → Service → Repository → BD, conforme exigido pela disciplina Desenvolvimento de Sistemas Web II.

## Technical Context

**Language/Version**: Java 21 (LTS) com Spring Boot 3.x

**Primary Dependencies**: Spring Web MVC, Thymeleaf, Spring Data JPA, Hibernate, Bean Validation (Hibernate Validator), PostgreSQL JDBC Driver

**Storage**: PostgreSQL (local ou Docker) na porta 5432

**Testing**: Não obrigatório para este escopo acadêmico (Bean Validation testado manualmente via formulário)

**Target Platform**: Servidor web local (porta 8080), navegador web moderno

**Project Type**: Web application monolítica (Spring MVC + Thymeleaf — server-side rendering)

**Performance Goals**: Adequado para uso acadêmico; sem requisito de concorrência elevada

**Constraints**: Stack restrito ao conteúdo até Aula 06 — sem API REST, sem Spring Security, sem JavaScript, sem DTOs, sem paginação, sem relacionamentos JPA

**Scale/Scope**: CRUD de uma única entidade (`Veiculo`); 5+ atributos; 2 páginas Thymeleaf (listagem + formulário)

## Constitution Check

*GATE: Must pass before Phase 0 research.*

A constituição do projeto está no template padrão (sem princípios customizados definidos). Gates verificados com base nos requisitos do trabalho:

| Gate | Status | Observação |
|------|--------|------------|
| Arquitetura em camadas (Controller → Service → Repository) | ✅ PASS | Estrutura obrigatória respeitada |
| Entidade com mínimo 5 atributos além do id | ✅ PASS | `Veiculo` tem 6 atributos |
| Bean Validation com ≥ 3 anotações distintas | ✅ PASS | `@NotBlank`, `@NotNull`, `@Min`/`@Max`, `@Size` planejados |
| Persistência em PostgreSQL | ✅ PASS | `spring.jpa.hibernate.ddl-auto=update` |
| Formulário Thymeleaf com `th:object`, `th:field`, `th:action` | ✅ PASS | Previsto no formulario.html |
| CRUD completo (Create/Read/Update/Delete) | ✅ PASS | Todos os endpoints mapeados |
| CSS próprio | ✅ PASS | `static/css/style.css` planejado |
| Maven como build tool | ✅ PASS | `pom.xml` com dependências Spring Boot |

**Resultado**: Sem violações. Prosseguir para Phase 1.

## Project Structure

### Documentation (this feature)

```text
specs/001-car-rental-system/
├── plan.md              # Este arquivo (/speckit-plan output)
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── web-routes.md    # Mapeamento de rotas HTTP/Thymeleaf
└── tasks.md             # Phase 2 output (/speckit-tasks — NÃO criado por /speckit-plan)
```

### Source Code (repository root)

```text
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── rental/
│   │           └── carros/
│   │               ├── controller/
│   │               │   └── VeiculoController.java
│   │               ├── model/
│   │               │   └── Veiculo.java
│   │               ├── service/
│   │               │   └── VeiculoService.java
│   │               ├── repository/
│   │               │   └── VeiculoRepository.java
│   │               └── CarrosApplication.java
│   └── resources/
│       ├── templates/
│       │   ├── lista.html        (listagem de veículos)
│       │   └── formulario.html   (cadastro/edição)
│       ├── static/
│       │   └── css/
│       │       └── style.css
│       └── application.properties
├── test/
│   └── java/
│       └── com/
│           └── rental/
│               └── carros/
│                   └── CarrosApplicationTests.java
└── pom.xml
```

**Structure Decision**: Single project — aplicação Spring Boot monolítica com server-side rendering via Thymeleaf. Sem separação backend/frontend pois toda renderização ocorre no servidor.
