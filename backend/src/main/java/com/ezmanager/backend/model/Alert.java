package com.ezmanager.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Alerts")
public class Alert {

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
        name = "parent_entity_id"
    )
    private UUID parentEntityId;

    @Column(
        name = "alert_date"
    )
    private LocalDateTime alertDate;

    @Column(
        name = "alert_message"
    )
    private String alertMessage;

    @Column(
        name = "created_at"
    )
    private LocalDateTime createdAt;

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

    public UUID getParentEntityId() {
        return parentEntityId;
    }
    public void setParentEntityId(UUID parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public LocalDateTime getAlertDate() {
        return alertDate;
    }
    public void setAlertDate(LocalDateTime alertDate) {
        this.alertDate = alertDate;
    }

    public String getAlertMessage() {
        return alertMessage;
    }
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
