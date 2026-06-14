package com.ezmanager.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ezmanager.backend.dto.CreateTaskRequest;
import com.ezmanager.backend.dto.TaskResponse;
import com.ezmanager.backend.dto.UpdateTaskRequest;
import com.ezmanager.backend.model.Task;
import com.ezmanager.backend.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<TaskResponse> getTasksByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByUserId(userId, pageable).map(TaskResponse::new);
    }

    public TaskResponse getTaskById(UUID taskId, UUID userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task non trovata"));
        if (!task.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato!");
        }
        return new TaskResponse(task);
    }

    public TaskResponse createTask(CreateTaskRequest dto, UUID userId) {
        Task task = new Task();

        task.setUserId(userId);
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setDate(dto.getDate());
        // wholeDay default false se omesso, perché il model lo richiede non-null
        task.setWholeDay(dto.getWholeDay() != null ? dto.getWholeDay() : false);
        task.setPriority(dto.getPriority());
        task.setSubtaskOf(dto.getSubtaskOf());
        task.setCreatedAt(LocalDateTime.now());

        return new TaskResponse(taskRepository.save(task));
    }

    public void deleteTask(UUID taskId, UUID userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task non trovata"));

        if (!task.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato");
        }

        taskRepository.deleteById(taskId);
    }

    public TaskResponse updateTask(UUID taskId, UpdateTaskRequest dto, UUID userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task non trovata"));

        if (!task.getUserId().equals(userId)) {
            new RuntimeException("Non autorizzato");
        }

        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setDate(dto.getDate());
        task.setWholeDay(dto.getWholeDay());
        task.setPriority(dto.getPriority());
        task.setSubtaskOf(dto.getSubtaskOf());

        return new TaskResponse(taskRepository.save(task));
    }
}
