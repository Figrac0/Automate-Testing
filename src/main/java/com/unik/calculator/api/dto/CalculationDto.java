package com.unik.calculator.api.dto;

import java.time.OffsetDateTime;

public class CalculationDto {
    private long id;

    private String leftValue;
    private String leftRadix;

    private String rightValue;
    private String rightRadix;

    private String operation;

    private String resultValue;
    private String resultRadix;

    private OffsetDateTime createdAt;

    public CalculationDto(
            long id,
            String leftValue, String leftRadix,
            String rightValue, String rightRadix,
            String operation,
            String resultValue, String resultRadix,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.leftValue = leftValue;
        this.leftRadix = leftRadix;
        this.rightValue = rightValue;
        this.rightRadix = rightRadix;
        this.operation = operation;
        this.resultValue = resultValue;
        this.resultRadix = resultRadix;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public String getLeftValue() { return leftValue; }
    public String getLeftRadix() { return leftRadix; }
    public String getRightValue() { return rightValue; }
    public String getRightRadix() { return rightRadix; }
    public String getOperation() { return operation; }
    public String getResultValue() { return resultValue; }
    public String getResultRadix() { return resultRadix; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
