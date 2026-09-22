# 🚗 Sistema de Aluguel de Carros

## Nome do Aluno

Gustavo Andrade

## Nome do Sistema

Sistema de Aluguel de Carros

## Descrição

Aplicação Web para gerenciamento do catálogo de veículos de uma locadora. Permite cadastrar, listar, editar e excluir veículos com validação de dados e persistência em banco de dados relacional.

## Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.3.4 |
| Spring MVC | Incluído no Spring Boot |
| Thymeleaf | Incluído no Spring Boot |
| Bean Validation (Hibernate Validator) | Incluído no Spring Boot |
| Spring Data JPA | Incluído no Spring Boot |
| Hibernate | Incluído no Spring Boot |
| PostgreSQL | 14+ |
| Maven | 3.6+ |

## Nome do Banco de Dados

`aluguel_carros`

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/rental/carros/
│   │   ├── controller/
│   │   │   ├── VeiculoController.java   # Rotas HTTP CRUD
│   │   │   └── HomeController.java      # Redirect / → /veiculos
│   │   ├── model/
│   │   │   └── Veiculo.java             # Entidade JPA + Bean Validation
│   │   ├── service/
│   │   │   └── VeiculoService.java      # Lógica de negócio
│   │   ├── repository/
│   │   │   └── VeiculoRepository.java   # Acesso ao banco (JpaRepository)
│   │   └── CarrosApplication.java       # Classe principal Spring Boot
│   └── resources/
│       ├── templates/
│       │   ├── lista.html               # Listagem de veículos
│       │   └── formulario.html          # Cadastro e edição
│       ├── static/css/
│       │   └── style.css                # Estilos próprios
│       └── application.properties       # Configuração do banco
└── pom.xml
```

## Instruções de Execução

### Pré-requisitos

- Java 17 ou superior instalado
- Maven 3.6+ instalado
- PostgreSQL 14+ rodando localmente (ou Docker)

### 1. Criar o banco de dados

**PostgreSQL local:**
```sql
psql -U postgres
CREATE DATABASE aluguel_carros;
```

**Docker (alternativa):**
```bash
docker run --name postgres-rental -e POSTGRES_DB=aluguel_carros -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
```

### 2. Configurar credenciais

Editar `src/main/resources/application.properties` se necessário:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/aluguel_carros
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

### 4. Acessar no navegador

```
http://localhost:8080
```

> A tabela `veiculo` é criada automaticamente na primeira execução.

## Funcionalidades

- ✅ **Cadastrar** veículo com formulário Thymeleaf validado
- ✅ **Listar** todos os veículos em tabela
- ✅ **Editar** veículo existente (formulário pré-preenchido)
- ✅ **Excluir** veículo com confirmação
- ✅ **Validação** Bean Validation com mensagens em português
- ✅ **Persistência** em PostgreSQL via Spring Data JPA

## Arquitetura

```
Controller → Service → Repository → PostgreSQL
```

## Disciplina

Desenvolvimento de Sistemas Web II
