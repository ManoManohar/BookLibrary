package com.example.demo.DTO;

import java.time.LocalDateTime;

public class ErrorDTO {
    private LocalDateTime localDateTime;
    private String message;
    private int status;

    public ErrorDTO(LocalDateTime localDateTime, String message, int status) {
        this.localDateTime = localDateTime;
        this.message = message;
        this.status = status;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
