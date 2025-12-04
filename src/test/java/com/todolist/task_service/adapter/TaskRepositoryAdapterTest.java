package com.todolist.task_service.adapter;

import com.todolist.task_service.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryAdapterTest {

    @Mock
    private TaskRepository repository;

    private TaskRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new TaskRepositoryAdapter(repository);
    }

    @Test
    void shouldSaveTask() {
        Task task = new Task(null, "Titulo", "Desc", false);
        Task saved = new Task(1L, "Titulo", "Desc", false);

        when(repository.save(task)).thenReturn(Mono.just(saved));

        StepVerifier.create(adapter.save(task))
                .expectNext(saved)
                .verifyComplete();

        verify(repository).save(task);
    }

    @Test
    void shouldFindAllTasks() {
        Task task1 = new Task(1L, "Titulo 1", "Desc 1", false);
        Task task2 = new Task(2L, "Titulo 2", "Desc 2", true);

        when(repository.findAll()).thenReturn(Flux.just(task1, task2));

        StepVerifier.create(adapter.findAll())
                .expectNext(task1)
                .expectNext(task2)
                .verifyComplete();

        verify(repository).findAll();
    }
    @Test
    void shouldFindById() {
        Task task = new Task(1L, "Tarefa", "Descrição", false);

        when(repository.findById(1L)).thenReturn(Mono.just(task));

        StepVerifier.create(adapter.findById(1L))
                .expectNext(task)
                .verifyComplete();

        verify(repository).findById(1L);
    }

    @Test
    void shouldDeleteById() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById(1L))
                .verifyComplete();

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldUpdateExistingTask() {
        Task existing = new Task(1L, "Antigo", "Antiga descrição", false);
        Task updated = new Task(1L, "Novo", "Nova descrição", true);

        when(repository.findById(1L)).thenReturn(Mono.just(existing));
        when(repository.save(updated)).thenReturn(Mono.just(updated));

        StepVerifier.create(adapter.update(1L, updated))
                .expectNext(updated)
                .verifyComplete();

        verify(repository).findById(1L);
        verify(repository).save(updated);
    }

    @Test
    void shouldNotUpdateWhenTaskDoesNotExist() {
        Long taskId = 999L;
        Task updatedTask = new Task(taskId, "Updated", "Updated desc", false);

        when(repository.findById(taskId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.update(taskId, updatedTask))
                .verifyComplete();
    }
}
