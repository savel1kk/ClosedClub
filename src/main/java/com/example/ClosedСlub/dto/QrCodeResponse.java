package com.example.ClosedСlub.dto;

import java.util.UUID;

public class QrCodeResponse {

    private Long id;
    private UUID uuid;
    private Long userId;
    private boolean active;



    public QrCodeResponse() {}

    public QrCodeResponse(Long id, UUID uuid, Long userId, boolean active) {
        this.id = id;
        this.uuid = uuid;
        this.userId = userId;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
