package com.todolist.task_service.service;

import com.todolist.task_service.adapter.TaskRepositoryPort;
import com.todolist.task_service.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepositoryPort repository;

    public Mono<Task> create(Task task) {
        return repository.save(task);
    }

    public Flux<Task> getAll() {
        return repository.findAll();
    }

    public Mono<Task> getById(Long id) {
        return repository.findById(id);
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    public Mono<Task> update(Long id, Task task) {
        return repository.update(id, task);
    }
}
