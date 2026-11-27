# AutoBox Parts Service

API REST para gerenciamento de oficina mecânica, desenvolvida com Java e Spring Boot. O sistema permite administrar clientes, veículos, peças, vendas e ordens de serviço.

## Funcionalidades

- Autenticação de usuários
- Cadastro e consulta de clientes
- Cadastro e consulta de veículos
- Gerenciamento de peças e estoque
- Registro de vendas
- Criação e atualização de ordens de serviço
- Adição de peças às ordens de serviço
- Alteração do status das ordens de serviço
- Tratamento centralizado de erros e validações

## Tecnologias

- Java 21
- Spring Boot 4.2.0
- Spring Web MVC
- Spring Data JPA
- Spring Security / OAuth2 Resource Server
- SpringDoc OpenAPI / Swagger UI
- Bean Validation
- Hibernate
- Flyway
- Lombok
- MySQL
- H2 para testes
- Maven
- JUnit e Mockito

## Pré-requisitos

- JDK 21
- Maven 3.9 ou superior
- MySQL 8 ou superior

## Configuração

A aplicação utiliza as seguintes variáveis de ambiente:

| Variável | Descrição | Padrão |
|---|---|---|
| `DB_URL` | URL JDBC do banco | `jdbc:mysql://localhost:3306/autobox_service` |
| `DB_USERNAME` | Usuário do banco | `root` |
| `DB_PASSWORD` | Senha do banco | *(não definida)* |

Exemplo no PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/autobox_service"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="sua_senha"
```

Crie o banco antes de iniciar:

```sql
CREATE DATABASE autobox_service;
```

A aplicação está configurada para executar na porta `8081` e para usar Hibernate com `ddl-auto=update`. O Flyway está desativado no arquivo `application.properties` atual.

Na inicialização, o projeto carrega automaticamente dados fictícios quando os registros ainda não existem: usuário administrador, clientes, veículos e peças.

## Modelo de dados

O banco foi modelado com as seguintes tabelas:

- `usuarios`
- `clientes`
- `veiculos`
- `pecas`
- `ordens_servico`
- `itens_ordem_servico`
- `vendas`
- `itens_venda`

A migration está em `src/main/resources/db/migration/V1__criar_tabelas_iniciais.sql` e contém chaves primárias, chaves estrangeiras, exclusividade de registros e validações de estoque, quantidade e preço.

### Principais campos

| Entidade | Campos relevantes |
|---|---|
| Usuário | `nome`, `email`, `senha_hash`, `perfil`, `ativo` |
| Cliente | `nome`, `telefone`, `email`, `cpf_cnpj` |
| Veículo | `placa`, `marca`, `modelo`, `ano`, `quilometragem`, `cliente_id` |
| Peça | `sku`, `nome`, `fabricante`, `aplicacao_veiculo`, `preco_unitario`, `quantidade_estoque`, `estoque_minimo`, `versao` |
| Ordem de serviço | `numero_os`, `veiculo_id`, `status`, `descricao_problema`, `valor_mao_de_obra`, `valor_total`, `estoque_baixado`, `data_abertura`, `data_conclusao` |
| Venda | `cliente_id`, `data_venda`, `valor_total` |
| Itens | `quantidade` e `preco_unitario_snapshot` |

## Alterações e correções recentes

- As entidades JPA agora usam os nomes das tabelas do modelo MySQL.
- Foi corrigido o repositório de ordens de serviço, removendo a consulta inválida `findByNumeroOs` enquanto a entidade não possuía o campo correspondente.
- `OrdemServico` passou a possuir `numeroOs`, valores financeiros, datas de abertura/conclusão e controle de baixa de estoque.
- `Peca` passou a possuir `sku`, fabricante, aplicação do veículo, estoque mínimo e controle otimista por `@Version`.
- `Usuario` passou a usar `senha_hash` e inclui os campos `ativo` e `nome`.
- `Cliente` passou a usar `cpf_cnpj` e exige telefone.
- `Veiculo` passou a incluir quilometragem e exige vínculo com um cliente.
- Vendas e itens de venda usam `valor_total` e snapshots de preço.
- Itens de ordens de serviço utilizam snapshots de preço e são removidos junto com a ordem pelo banco.
- A criação de uma peça gera automaticamente um SKU único no formato `SKU-XXXXXXXXXXXX`.
- A criação de uma ordem gera automaticamente um número único no formato `OS-<timestamp>`.
- Os nomes das colunas estrangeiras foram padronizados: `cliente_id`, `veiculo_id`, `venda_id`, `ordem_servico_id` e `peca_id`.

## Executando o projeto

No diretório raiz do projeto:

```powershell
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8081
```

O Swagger/OpenAPI ficará disponível em:

- Interface Swagger: `http://localhost:8081/swagger-ui/index.html`
- Especificação JSON: `http://localhost:8081/v3/api-docs`

Para gerar o executável:

```powershell
mvn clean package
java -jar target/API-0.0.1-SNAPSHOT.jar
```

## Executando os testes

```powershell
mvn test
```

Os testes usam o banco H2 em memória.

## Dados de demonstração

Ao iniciar a aplicação, são disponibilizados dados fictícios para apresentação:

- Usuário administrador: `admin@autobox.com`
- Senha: `123456`
- 3 clientes
- 3 veículos
- 5 peças

## Endpoints principais

### Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/auth/login` | Autentica um usuário |

### Clientes

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/clientes` | Lista clientes |
| `GET` | `/api/clientes/{id}` | Busca um cliente |
| `POST` | `/api/clientes` | Cria um cliente |
| `PUT` | `/api/clientes/{id}` | Atualiza um cliente |
| `DELETE` | `/api/clientes/{id}` | Remove um cliente |

### Veículos

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/veiculos` | Lista veículos |
| `GET` | `/api/veiculos/{id}` | Busca um veículo |
| `POST` | `/api/veiculos` | Cria um veículo |
| `PUT` | `/api/veiculos/{id}` | Atualiza um veículo |
| `DELETE` | `/api/veiculos/{id}` | Remove um veículo |

### Peças

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/pecas` | Lista peças |
| `GET` | `/api/pecas/{id}` | Busca uma peça |
| `POST` | `/api/pecas` | Cadastra uma peça |
| `DELETE` | `/api/pecas/{id}` | Remove uma peça |

### Ordens de serviço

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/ordens-servico` | Lista ordens de serviço |
| `GET` | `/api/ordens-servico/{id}` | Busca uma ordem de serviço |
| `POST` | `/api/ordens-servico` | Cria uma ordem de serviço |
| `POST` | `/api/ordens-servico/{id}/pecas` | Adiciona uma peça à ordem |
| `PATCH` | `/api/ordens-servico/{id}/status` | Atualiza o status da ordem |

### Vendas

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/vendas` | Registra uma venda |

## Status de uma ordem de serviço

- `ABERTA`
- `EM_ANDAMENTO`
- `AGUARDANDO_PECA`
- `CONCLUIDA`
- `CANCELADA`

## Perfis de usuário

- `ADMIN`
- `ATENDENTE`
- `MECANICO`

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/AutoBox_PartsService/
│   │   ├── application/service/
│   │   ├── controller/
│   │   ├── domain/entity/
│   │   ├── domain/enums/
│   │   ├── domain/exception/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── infrastructure/
│   │   └── mapper/
│   └── resources/
│       ├── db/migration/
│       └── application.properties
└── test/
    └── java/AutoBox_PartsService/
