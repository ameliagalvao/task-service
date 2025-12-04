package com.todolist.task_service.controller;

import com.todolist.task_service.domain.Task;
import com.todolist.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Mono<ResponseEntity<Task>> create(@RequestBody Task task) {
        return taskService.create(task)
                .map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED));
    }

    @GetMapping
    public Flux<Task> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Task>> getById(@PathVariable Long id) {
        return taskService.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Task>> update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return taskService.delete(id)
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
