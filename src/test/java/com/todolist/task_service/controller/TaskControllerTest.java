package com.todolist.task_service.controller;

import com.todolist.task_service.domain.Task;
import com.todolist.task_service.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class TaskControllerTest {

    private TaskService taskService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        taskService = Mockito.mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void testGetAll() {
        Mockito.when(taskService.getAll()).thenReturn(Flux.just(new Task(1L, "Test Task", "Desc", false)));

        webTestClient.get().uri("/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class).hasSize(1);
    }

    @Test
    void testCreate() {
        Task task = new Task(null, "Test Task", "Desc", false);

        Mockito.when(taskService.create(any(Task.class)))
                .thenReturn(Mono.just(task));

        webTestClient.post().uri("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(task)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Test Task");
    }

    @Test
    void testUpdate() {
        Task task = new Task(1L, "Updated", "Updated Description", true);

        Mockito.when(taskService.update(eq(1L), any(Task.class)))
                .thenReturn(Mono.just(task));

        webTestClient.put().uri("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(task)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Updated")
                .jsonPath("$.description").isEqualTo("Updated Description")
                .jsonPath("$.completed").isEqualTo(true);
    }
}

