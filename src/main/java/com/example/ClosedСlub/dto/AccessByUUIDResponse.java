package com.example.ClosedСlub.dto;

public class AccessByUUIDResponse {

    private String userName;
    private String message;

    public AccessByUUIDResponse(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
