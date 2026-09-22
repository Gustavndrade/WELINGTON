# Quickstart: Validação do Sistema de Aluguel de Carros

**Feature**: `001-car-rental-system`
**Phase**: 1 — Design & Contracts
**Date**: 2026-09-22

## Pré-requisitos

| Requisito | Versão Mínima | Verificação |
|---|---|---|
| Java JDK | 17+ (recomendado 21) | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| PostgreSQL | 14+ (ou Docker) | `psql --version` |

## Setup do Banco de Dados

### Opção A: PostgreSQL Local

```sql
-- Conectar ao PostgreSQL como superusuário
psql -U postgres

-- Criar banco de dados
CREATE DATABASE aluguel_carros;

-- (Opcional) Criar usuário dedicado
CREATE USER rental_user WITH PASSWORD 'rental123';
GRANT ALL PRIVILEGES ON DATABASE aluguel_carros TO rental_user;
```

### Opção B: Docker

```bash
docker run --name postgres-rental \
  -e POSTGRES_DB=aluguel_carros \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15
```

## Configuração (`application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/aluguel_carros
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.thymeleaf.cache=false
server.port=8080
```

> A tabela `veiculo` será criada automaticamente na primeira execução graças ao `ddl-auto=update`.

## Inicialização da Aplicação

```bash
# Na raiz do projeto (onde está o pom.xml)
mvn spring-boot:run
```

Aguardar a mensagem: `Started CarrosApplication in X seconds`

Acessar: **http://localhost:8080/veiculos**

## Cenários de Validação

### Cenário 1 — Cadastrar veículo com dados válidos (FR-001, FR-004)

1. Acessar `http://localhost:8080/veiculos/novo`
2. Preencher todos os campos:
   - Modelo: `Civic`
   - Marca: `Honda`
   - Ano: `2022`
   - Placa: `ABC1234`
   - Valor Diária: `150.00`
   - Disponível: `Sim`
3. Clicar em "Salvar"
4. **Esperado**: Redirecionamento para `/veiculos` com o veículo "Civic - Honda" na tabela

---

### Cenário 2 — Validação de campos obrigatórios (FR-002, FR-003)

1. Acessar `http://localhost:8080/veiculos/novo`
2. Submeter o formulário **sem preencher nenhum campo**
3. **Esperado**: Formulário reexibido com mensagens de erro em todos os campos obrigatórios; nenhum dado salvo no BD

---

### Cenário 3 — Validação de ano fora do intervalo (FR-002)

1. Acessar `/veiculos/novo`
2. Preencher todos os campos, mas com `ano = 1800`
3. **Esperado**: Mensagem de erro no campo `ano`: "O ano deve ser entre 1900 e 2030"

---

### Cenário 4 — Listar veículos (FR-005)

1. Com ao menos um veículo cadastrado, acessar `http://localhost:8080/veiculos`
2. **Esperado**: Tabela com colunas Modelo, Marca, Ano, Placa, Valor Diária, Disponível, Ações

---

### Cenário 5 — Editar veículo (FR-006)

1. Na listagem, clicar em "Editar" de um veículo
2. **Esperado**: Formulário pré-preenchido com os dados do veículo selecionado
3. Alterar o campo `valorDiaria` para `200.00` e salvar
4. **Esperado**: Redirecionamento para listagem com valor atualizado

---

### Cenário 6 — Excluir veículo (FR-007)

1. Na listagem, clicar em "Excluir" de um veículo
2. **Esperado**: Veículo removido da tabela imediatamente

---

### Cenário 7 — Preservação de valores com erro (FR-003)

1. Preencher formulário com dados parcialmente válidos (ex: modelo "Corolla", marca em branco)
2. Submeter
3. **Esperado**: Formulário reexibido com `modelo` mantendo "Corolla" e mensagem de erro apenas no campo `marca`

## Verificação de Persistência no Banco

```sql
-- Conectar ao banco
psql -U postgres -d aluguel_carros

-- Verificar tabela criada
\dt

-- Listar registros
SELECT * FROM veiculo;
```

## Referências

- Modelo de dados: [data-model.md](../data-model.md)
- Contrato de rotas: [contracts/web-routes.md](../contracts/web-routes.md)
- Especificação: [spec.md](../spec.md)
