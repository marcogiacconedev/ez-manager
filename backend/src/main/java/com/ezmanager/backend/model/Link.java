package com.ezmanager.backend.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Links")
public class Link {
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
        nullable = false,
        name = "link"
    )
    private String link;

    @Column(
        nullable = false,
        name = "created_at"
    )
    private String createdAt;


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

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
