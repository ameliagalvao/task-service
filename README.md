# Task Service

Microsserviço responsável pelo gerenciamento de tarefas (To-Do) da aplicação.

## Funcionalidades

- Criar tarefas
- Listar todas as tarefas
- Buscar tarefa por ID
- Atualizar tarefa existente
- Remover tarefa
- Integração com banco PostgreSQL via R2DBC

## Tecnologias

- Java 21
- Spring WebFlux
- Spring Data R2DBC
- PostgreSQL
- Testcontainers (integração)
- JUnit 5 + Mockito

## Testes

- `mvn clean install` executa testes unitários e de integração (Testcontainers)

## Executando localmente

Configure um banco PostgreSQL ou utilize o ambiente do Kubernetes com Minikube.

Exemplo de properties:
```properties
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/todolist
spring.r2dbc.username=postgres
spring.r2dbc.password=postgres
```

## Endpoints REST

- `POST /tasks`
- `GET /tasks`
- `GET /tasks/{id}`
- `PUT /tasks/{id}`
- `DELETE /tasks/{id}`

## Estrutura Principal

- `domain`: modelo de dados
- `adapter`: repositórios e interfaces
- `controller`: endpoints REST
- `service`: regras de negócio
