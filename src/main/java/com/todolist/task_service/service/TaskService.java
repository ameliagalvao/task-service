package com.todolist.task_service.service;

import com.todolist.task_service.model.Task;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Task create(Task task) {
        task.setId(idGenerator.incrementAndGet());
        task.setCompleted(false);
        tasks.put(task.getId(), task);
        return task;
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task update(Long id, Task updatedTask) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
        }
        return task;
    }

    public void delete(Long id) {
        tasks.remove(id);
    }
}
