package com.ezmanager.backend.model;

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

    private UUID userId;
    @Column(
        nullable = false,
        name = "user_id"
    )

    private UUID parentEntityId;
    @Column(
        nullable = false,
        name = "parent_entity_id"
    )

    private String alertDate;
    @Column(
        nullable = false,
        name = "alert_date"
    )

    private String alertMessage;
    @Column(
        name = "alert_message"
    )
    private String createdAt;


    //getters
    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getParentEntityId() {
        return parentEntityId;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    
}
