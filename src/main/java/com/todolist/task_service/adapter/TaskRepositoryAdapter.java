package com.todolist.task_service.adapter;

import com.todolist.task_service.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskRepository repository;

    @Override
    public Mono<Task> save(Task task) {
        return repository.save(task);
    }

    @Override
    public Mono<Task> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Task> update(Long id, Task updatedTask) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setTitle(updatedTask.getTitle());
                    existing.setDescription(updatedTask.getDescription());
                    existing.setCompleted(updatedTask.isCompleted());
                    return repository.save(existing);
                })
                .switchIfEmpty(Mono.empty());
    }
}

