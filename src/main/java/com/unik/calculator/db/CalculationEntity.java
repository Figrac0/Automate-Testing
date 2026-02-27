package com.unik.calculator.db;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "calculations")
public class CalculationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "left_value", nullable = false)
    private String leftValue;

    @Column(name = "left_radix", nullable = false)
    private String leftRadix;

    @Column(name = "right_value", nullable = false)
    private String rightValue;

    @Column(name = "right_radix", nullable = false)
    private String rightRadix;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "result_value", nullable = false)
    private String resultValue;

    @Column(name = "result_radix", nullable = false)
    private String resultRadix;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public String getLeftValue() { return leftValue; }
    public void setLeftValue(String leftValue) { this.leftValue = leftValue; }

    public String getLeftRadix() { return leftRadix; }
    public void setLeftRadix(String leftRadix) { this.leftRadix = leftRadix; }

    public String getRightValue() { return rightValue; }
    public void setRightValue(String rightValue) { this.rightValue = rightValue; }

    public String getRightRadix() { return rightRadix; }
    public void setRightRadix(String rightRadix) { this.rightRadix = rightRadix; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }

    public String getResultRadix() { return resultRadix; }
    public void setResultRadix(String resultRadix) { this.resultRadix = resultRadix; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
