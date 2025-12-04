package com.todolist.task_service.integration;

import com.todolist.task_service.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class TaskServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("todolist")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () ->
                "r2dbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/" + postgres.getDatabaseName());
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "false"); // caso você use Flyway
    }

    @Autowired
    private WebTestClient webTestClient;

    private Task testTask;

    @BeforeEach
    void setup() {
        testTask = new Task(null, "Integration Test", "Testing with Testcontainers", false);
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        // Criação da task
        webTestClient.post()
                .uri("/tasks")
                .bodyValue(testTask)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.title").isEqualTo("Integration Test");

        // Listagem de todas as tasks
        webTestClient.get()
                .uri("/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .hasSize(1);
    }

    @Test
    void shouldReturnNotFoundForNonexistentTask() {
        webTestClient.get()
                .uri("/tasks/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}

