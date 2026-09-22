# Web Routes Contract: Sistema de Aluguel de Carros

**Feature**: `001-car-rental-system`
**Phase**: 1 — Design & Contracts
**Date**: 2026-09-22
**Type**: Spring MVC HTTP Routes + Thymeleaf View Contract

## Rotas HTTP (Controller Contract)

| # | Método HTTP | Rota | Handler Method | View Retornada | Descrição |
|---|---|---|---|---|---|
| 1 | GET | `/veiculos` | `listar()` | `lista` | Lista todos os veículos |
| 2 | GET | `/veiculos/novo` | `novo()` | `formulario` | Exibe formulário de criação |
| 3 | POST | `/veiculos` | `salvar(@Valid, BindingResult)` | redirect `/veiculos` ou `formulario` (erro) | Persiste novo veículo |
| 4 | GET | `/veiculos/{id}/editar` | `editar(@PathVariable id)` | `formulario` | Exibe formulário pré-preenchido |
| 5 | POST | `/veiculos/{id}` | `atualizar(@Valid, BindingResult, @PathVariable id)` | redirect `/veiculos` ou `formulario` (erro) | Atualiza veículo |
| 6 | POST | `/veiculos/{id}/excluir` | `excluir(@PathVariable id)` | redirect `/veiculos` | Remove veículo |

## Model Attributes (dados enviados às views)

### View `lista`
| Atributo | Tipo | Descrição |
|---|---|---|
| `veiculos` | `List<Veiculo>` | Lista completa de veículos do BD |

### View `formulario`
| Atributo | Tipo | Descrição |
|---|---|---|
| `veiculo` | `Veiculo` | Objeto do formulário (novo vazio ou existente para edição) |

## Thymeleaf Template Contract

### `lista.html`
- Iteração: `th:each="v : ${veiculos}"`
- Link para editar: `th:href="@{/veiculos/{id}/editar(id=${v.id})}"`
- Formulário de exclusão: `th:action="@{/veiculos/{id}/excluir(id=${v.id})}"` method POST
- Link para novo: `th:href="@{/veiculos/novo}"`

### `formulario.html`
- Bind do objeto: `th:object="${veiculo}"`
- Ação do form (criação): `th:action="@{/veiculos}"` method POST
- Ação do form (edição): `th:action="@{/veiculos/{id}(id=${veiculo.id})}"` method POST
- Campos: `th:field="*{modelo}"`, `th:field="*{marca}"`, `th:field="*{ano}"`, `th:field="*{placa}"`, `th:field="*{valorDiaria}"`, `th:field="*{disponivel}"`
- Erros: `th:errors="*{campo}"` com class CSS de erro

## Fluxos de Validação

```text
POST /veiculos
  ↓
@Valid Veiculo + BindingResult
  ↓ sem erros → VeiculoService.salvar() → redirect:/veiculos
  ↓ com erros → return "formulario" (valores preservados via th:field)
```

## Mensagens de Erro Esperadas por Campo

| Campo | Mensagem de Erro |
|---|---|
| `modelo` | "O modelo é obrigatório" / "O modelo deve ter entre 2 e 100 caracteres" |
| `marca` | "A marca é obrigatória" / "A marca deve ter entre 2 e 60 caracteres" |
| `ano` | "O ano é obrigatório" / "O ano deve ser entre 1900 e 2030" |
| `placa` | "A placa é obrigatória" / "A placa deve ter entre 7 e 8 caracteres" |
| `valorDiaria` | "O valor da diária é obrigatório" / "O valor da diária deve ser maior que zero" |
| `disponivel` | "Informe a disponibilidade do veículo" |
