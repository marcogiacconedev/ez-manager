package com.ezmanager.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class CreateTaskRequest {

    @NotBlank(message = "Il nome della task è obbligatorio")
    private String name;

    private String description;
    private LocalDate date;
    private Boolean wholeDay;
    private Integer priority;
    private UUID subtaskOf;
    private LocalDateTime completedAt;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Boolean getWholeDay() { return wholeDay; }
    public void setWholeDay(Boolean wholeDay) { this.wholeDay = wholeDay; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public UUID getSubtaskOf() { return subtaskOf; }
    public void setSubtaskOf(UUID subtaskOf) { this.subtaskOf = subtaskOf; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
