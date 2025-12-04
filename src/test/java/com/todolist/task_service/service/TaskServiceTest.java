package com.todolist.task_service.service;

import com.todolist.task_service.adapter.TaskRepositoryPort;
import com.todolist.task_service.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    private TaskRepositoryPort repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = mock(TaskRepositoryPort.class);
        service = new TaskService(repository);
    }

    @Test
    void create_shouldReturnSavedTask() {
        Task task = new Task(null, "Title", "Description", false);
        Task savedTask = new Task(1L, "Title", "Description", false);

        when(repository.save(task)).thenReturn(Mono.just(savedTask));

        StepVerifier.create(service.create(task))
                .expectNext(savedTask)
                .verifyComplete();
    }

    @Test
    void getAll_shouldReturnAllTasks() {
        when(repository.findAll()).thenReturn(Flux.just(
                new Task(1L, "Task 1", "Desc 1", false),
                new Task(2L, "Task 2", "Desc 2", true)
        ));

        StepVerifier.create(service.getAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getById_shouldReturnTask() {
        Task task = new Task(1L, "Title", "Description", false);
        when(repository.findById(1L)).thenReturn(Mono.just(task));

        StepVerifier.create(service.getById(1L))
                .expectNext(task)
                .verifyComplete();
    }

    @Test
    void delete_shouldCompleteWithoutError() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(1L))
                .verifyComplete();
    }

    @Test
    void update_shouldReturnUpdatedTask() {
        Task task = new Task(1L, "Updated", "Updated Desc", true);
        when(repository.update(1L, task)).thenReturn(Mono.just(task));

        StepVerifier.create(service.update(1L, task))
                .expectNext(task)
                .verifyComplete();
    }
}
