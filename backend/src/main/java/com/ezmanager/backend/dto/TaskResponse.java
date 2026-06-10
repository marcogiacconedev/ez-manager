package com.ezmanager.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ezmanager.backend.model.Task;

public class TaskResponse {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime date;
    private Boolean wholeDay;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Integer priority;
    private UUID subtaskOf;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.date = task.getDate();
        this.wholeDay = task.getWholeDay();
        this.createdAt = task.getCreatedAt();
        this.completedAt = task.getCompletedAt();
        this.priority = task.getPriority();
        this.subtaskOf = task.getSubtaskOf();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getDate() { return date; }
    public Boolean getWholeDay() { return wholeDay; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public Integer getPriority() { return priority; }
    public UUID getSubtaskOf() { return subtaskOf; }
}
