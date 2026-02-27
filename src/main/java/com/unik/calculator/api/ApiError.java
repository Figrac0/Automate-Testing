package com.unik.calculator.api;

import java.time.OffsetDateTime;

public class ApiError {
    private int status;
    private String message;
    private OffsetDateTime timestamp;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}
