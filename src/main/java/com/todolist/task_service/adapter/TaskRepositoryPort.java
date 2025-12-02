package com.todolist.task_service.adapter;

import com.todolist.task_service.domain.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepositoryPort {
    Mono<Task> save(Task task);
    Mono<Task> findById(Long id);
    Flux<Task> findAll();
    Mono<Void> deleteById(Long id);
    Mono<Task> update(Long id, Task updatedTask);
}
