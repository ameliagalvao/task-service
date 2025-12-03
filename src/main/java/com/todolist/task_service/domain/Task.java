package com.todolist.task_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tasks")
@AllArgsConstructor
public class Task {
    @Id
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
