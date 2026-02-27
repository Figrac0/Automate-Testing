package com.unik.calculator.api.dto;

import java.time.OffsetDateTime;

public class CalcResponse {
    private long id;
    private String result;
    private String resultRadix;
    private OffsetDateTime createdAt;

    public CalcResponse(long id, String result, String resultRadix, OffsetDateTime createdAt) {
        this.id = id;
        this.result = result;
        this.resultRadix = resultRadix;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public String getResult() { return result; }
    public String getResultRadix() { return resultRadix; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
