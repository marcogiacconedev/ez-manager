package com.ezmanager.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(
        nullable = false,    
        name = "user_id"
    )
    private UUID userId;

    @Column(
        nullable = false,
        name = "name"
    )
    private String name;

    @Column(
        name = "description"
    )
    private String description;
    
    @Column(
        name = "date"
    )
    private LocalDateTime date;

    @Column(
        nullable = false,
        name = "whole_day"
    )
    private Boolean wholeDay;

    @Column(
        nullable = false,
        name = "created_at"
    )
    private LocalDateTime createdAt;

    @Column(
        nullable = true,
        name = "completed_at"
    )
    private LocalDateTime completedAt;

    @Column(
        name = "priority"
    )
    private Integer priority;

    @Column(
        nullable = true,
        name = "subtask_of"
    )
    private UUID subtaskOf;


    //getters and setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Boolean getWholeDay() {
        return wholeDay;
    }
    public void setWholeDay(Boolean wholeDay) {
        this.wholeDay = wholeDay;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public UUID getSubtaskOf() {
        return subtaskOf;
    }
    public void setSubtaskOf(UUID subtaskOf) {
        this.subtaskOf = subtaskOf;
    }
}
