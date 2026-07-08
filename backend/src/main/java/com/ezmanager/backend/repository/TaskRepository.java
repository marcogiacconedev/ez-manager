package com.ezmanager.backend.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezmanager.backend.model.Task;

public interface TaskRepository extends JpaRepository<Task, UUID>{
    Page<Task> findByUserId(UUID userId, Pageable pageable);
    Page<Task> findByUserIdAndDate(UUID userId, LocalDate date, Pageable pageable);
}
