package com.todolist.task_service.controller;

import com.todolist.task_service.domain.Task;
import com.todolist.task_service.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = TaskController.class)
class TaskControllerTest {

    @MockitoBean
    private TaskService taskService;

    private WebTestClient webClient;

    @BeforeEach
    void setup() {
        webClient = WebTestClient.bindToController(new TaskController(taskService)).build();
    }

    @Test
    void create_shouldReturnCreatedTask() {
        Task task = new Task(null, "New Task", "Test", false);
        Task savedTask = new Task(1L, "New Task", "Test", false);

        Mockito.when(taskService.create(task)).thenReturn(Mono.just(savedTask));

        webClient.post().uri("/tasks")
                .bodyValue(task)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void getAll_shouldReturnAllTasks() {
        Mockito.when(taskService.getAll()).thenReturn(Flux.just(
                new Task(1L, "Task 1", "Desc 1", false),
                new Task(2L, "Task 2", "Desc 2", true)
        ));

        webClient.get().uri("/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .hasSize(2);
    }

    @Test
    void getById_shouldReturnTaskIfExists() {
        Task task = new Task(1L, "Task", "Description", false);
        Mockito.when(taskService.getById(1L)).thenReturn(Mono.just(task));

        webClient.get().uri("/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void getById_shouldReturnNotFoundIfNotExists() {
        Mockito.when(taskService.getById(99L)).thenReturn(Mono.empty());

        webClient.get().uri("/tasks/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void update_shouldReturnUpdatedTask() {
        Task updatedTask = new Task(1L, "Updated", "Updated Desc", true);
        Mockito.when(taskService.update(1L, updatedTask)).thenReturn(Mono.just(updatedTask));

        webClient.put().uri("/tasks/1")
                .bodyValue(updatedTask)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Updated");
    }

    @Test
    void delete_shouldReturnNoContent() {
        Mockito.when(taskService.delete(1L)).thenReturn(Mono.empty());

        webClient.delete().uri("/tasks/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}

