package com.ezmanager.backend.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezmanager.backend.dto.CreateTaskRequest;
import com.ezmanager.backend.dto.TaskResponse;
import com.ezmanager.backend.dto.UpdateTaskRequest;
import com.ezmanager.backend.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal String userId) {

        return ResponseEntity.ok(taskService.getTasksByUserId(UUID.fromString(userId), page, size));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest dto,
            @AuthenticationPrincipal String userId) {

        TaskResponse created = taskService.createTask(dto, UUID.fromString(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
        @PathVariable UUID taskId,
        @AuthenticationPrincipal String userId
    ) {
        taskService.deleteTask(taskId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
        @PathVariable UUID taskId,
        @Valid @RequestBody UpdateTaskRequest dto,
        @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.ok(taskService.updateTask(taskId, dto, UUID.fromString(userId)));
    }
}
