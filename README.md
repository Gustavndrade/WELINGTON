# Sistema de Aluguel de Carros

## Nome do Aluno

Gustavo Andrade

## Nome do Sistema

Sistema de Aluguel de Carros

## Descrição

Aplicação web para gerenciar a frota de uma locadora, com **login**, **perfis de acesso**
(ADMIN e CLIENTE), **cadastro público de contas**, **aluguel autenticado** e
**cancelamento com histórico**.

O administrador cadastra veículos e clientes. O cliente vê o catálogo, aluga com a
própria conta e cancela antes da retirada sem apagar o registro.

## Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.13 |
| Spring MVC, Thymeleaf, Validation, Data JPA | Incluídos no Spring Boot |
| Spring Security | BCrypt, login por formulário, CSRF ligado |
| PostgreSQL | 14+ |
| Maven | 3.6+ |

## Nome do Banco de Dados

`aluguel_carros`

## Como executar

1. Crie o banco no PostgreSQL (porta padrão **5432**):

   ```sql
   CREATE DATABASE aluguel_carros;
   ```

   Os valores padrão ficam em `src/main/resources/application.properties`
   (usuário `postgres`, senha `123`). Para outros valores, defina `DB_URL`,
   `DB_USERNAME` e `DB_PASSWORD`.

2. Execute:

   ```bash
   mvn spring-boot:run
   ```

3. Acesse <http://localhost:8080>. Sem sessão, o navegador vai para `/login`.

O Hibernate cria e atualiza as tabelas (`veiculo`, `cliente`, `usuario`, `rental_record`).

## Contas de demonstração

Criadas na primeira execução (`config/CargaInicial.java`), se ainda não existirem:

| Perfil | E-mail | Senha |
|---|---|---|
| ADMIN | `admin@aluguel.com` | `admin123` |
| CLIENTE | `cliente@aluguel.com` | `teste123` |

O administrador pode ser trocado por `APP_ADMIN_EMAIL` e `APP_ADMIN_PASSWORD`.
**Essas credenciais servem apenas para a aula.** O cadastro público em `/cadastro`
sempre cria uma conta CLIENTE, já vinculada a um `Cliente`.

## Perfis e rotas

| Rota | Acesso |
|---|---|
| `/login`, `/cadastro`, `/css/**`, `/error` | Público |
| `/catalogo`, `/meus-alugueis`, `POST /alugueis/meu`, `POST /meus-alugueis/{id}/cancelar` | CLIENTE |
| `/veiculos/**`, `/clientes/**`, `/alugueis/**`, `/relatorios/**` | ADMIN |
| `/` e `POST /logout` | Qualquer usuário autenticado |

As rotas do cliente são declaradas **antes** de `/alugueis/**`, para que
`POST /alugueis/meu` não seja bloqueada. O cliente do aluguel sai da conta
logada, nunca do formulário. O formulário do catálogo pede só as datas.

## Fluxo do cliente

1. Cria a conta em `/cadastro` (nome, e-mail, telefone, documento, senha) e entra em `/login`.
2. Em `/catalogo` vê os veículos **disponíveis e sem aluguel ativo** e confirma a retirada e a devolução.
3. Em `/meus-alugueis` acompanha os aluguéis (ativos e cancelados) e pode cancelar antes do início.
4. O veículo cancelado volta a ficar disponível. O registro permanece com `status = CANCELADO`.

## Fluxo do administrador

Cadastra e edita veículos e clientes, registra aluguéis manualmente (as mesmas regras de
disponibilidade valem), consulta os aluguéis de cada veículo e o relatório geral, que separa
**aluguéis ativos** do **histórico completo**.

## Banco já existente

Se a tabela `rental_record` já foi usada antes desta versão, depois da primeira execução rode:

```sql
UPDATE rental_record SET status = 'ATIVO' WHERE status IS NULL;
```

Aluguéis antigos podem ficar sem `cliente_id`. Eles não entram no catálogo do cliente.
Contas criadas só como `usuario`, sem cliente vinculado, não conseguem alugar: use uma conta nova.

## Roteiro de testes manuais

- `/veiculos`, `/clientes` e `/alugueis` sem login redirecionam para `/login`.
- Senha incorreta mostra o aviso; credenciais corretas abrem a página inicial.
- Cadastro recusa campos vazios, e-mail inválido, senha curta, senhas diferentes e e-mail duplicado.
- CLIENTE acessando `/veiculos`, `/alugueis` ou `/relatorios` recebe **403**.
- Dois clientes não alugam o mesmo veículo ao mesmo tempo. Quem cancela libera o veículo.
- Uma conta não cancela o aluguel de outra, nem um aluguel já cancelado ou já iniciado.
- *Sair* encerra a sessão e exige novo login.
