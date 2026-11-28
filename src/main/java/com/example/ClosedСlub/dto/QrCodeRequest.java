package com.example.ClosedСlub.dto;

import jakarta.validation.constraints.NotNull;

public class QrCodeRequest {

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
