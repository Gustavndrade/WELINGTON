# Data Model: Sistema de Aluguel de Carros

**Feature**: `001-car-rental-system`
**Phase**: 1 — Design & Contracts
**Date**: 2026-09-22

## Entidades

### `Veiculo`

Representa um veículo disponível para locação no sistema.

**Tabela PostgreSQL**: `veiculo`

| Campo | Tipo Java | Tipo SQL | Nullable | Validações Bean Validation |
|---|---|---|---|---|
| `id` | `Long` | `BIGINT` (PK, auto-increment) | NOT NULL | — |
| `modelo` | `String` | `VARCHAR(100)` | NOT NULL | `@NotBlank`, `@Size(min=2, max=100)` |
| `marca` | `String` | `VARCHAR(60)` | NOT NULL | `@NotBlank`, `@Size(min=2, max=60)` |
| `ano` | `Integer` | `INTEGER` | NOT NULL | `@NotNull`, `@Min(1900)`, `@Max(2030)` |
| `placa` | `String` | `VARCHAR(8)` | NOT NULL | `@NotBlank`, `@Size(min=7, max=8)` |
| `valorDiaria` | `BigDecimal` | `NUMERIC(10,2)` | NOT NULL | `@NotNull`, `@DecimalMin("0.01")` |
| `disponivel` | `Boolean` | `BOOLEAN` | NOT NULL | `@NotNull` |

**Anotações JPA obrigatórias**:
- `@Entity` — marca a classe como entidade gerenciada pelo JPA
- `@Table(name = "veiculo")` — nome explícito da tabela
- `@Id` — chave primária
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` — auto-incremento via SERIAL do PostgreSQL

**Anotações Bean Validation utilizadas** (≥ 3 distintas ✅):
- `@NotBlank` — campos texto obrigatórios não podem ser nulos ou em branco
- `@NotNull` — campos numéricos/booleanos obrigatórios
- `@Size` — limites de tamanho para strings
- `@Min` / `@Max` — limites numéricos para o ano
- `@DecimalMin` — valor mínimo monetário para valorDiaria

## Relacionamentos

Nenhum relacionamento entre entidades nesta versão. Escopo limitado a CRUD simples de entidade única conforme requisito da disciplina (sem `@OneToMany`/`@ManyToOne`).

## Regras de Negócio e Validações

| Regra | Implementação |
|---|---|
| `modelo` obrigatório, 2–100 chars | `@NotBlank @Size(min=2, max=100)` |
| `marca` obrigatória, 2–60 chars | `@NotBlank @Size(min=2, max=60)` |
| `ano` obrigatório, entre 1900 e 2030 | `@NotNull @Min(1900) @Max(2030)` |
| `placa` obrigatória, 7–8 chars | `@NotBlank @Size(min=7, max=8)` |
| `valorDiaria` obrigatório, ≥ R$ 0,01 | `@NotNull @DecimalMin("0.01")` |
| `disponivel` obrigatório | `@NotNull` |
| Preservar valores ao reexibir formulário com erro | BindingResult + `th:field` do Thymeleaf |

## Transições de Estado

| Operação | Estado Anterior | Estado Posterior |
|---|---|---|
| Criar | — | Registro salvo no BD |
| Editar | Registro existente | Registro atualizado no BD |
| Excluir | Registro existente | Registro removido do BD |
| Alterar disponibilidade | `disponivel=true/false` | `disponivel=false/true` via edição |

## Camadas e Responsabilidades

```text
VeiculoController  →  recebe requisições HTTP, valida com @Valid, delega ao Service
VeiculoService     →  regras de negócio (salvar, listarTodos, buscarPorId, excluir)
VeiculoRepository  →  interface JpaRepository<Veiculo, Long> — acesso ao BD
Veiculo (entity)   →  mapeamento JPA + validações Bean Validation
```
