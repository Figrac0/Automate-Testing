package com.unik.calculator.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CalcRequest {

    @Valid
    @NotNull
    private Operand left;

    @Valid
    @NotNull
    private Operand right;

    @NotBlank
    private String op;

    @NotBlank
    private String resultRadix;

    public Operand getLeft() { return left; }
    public void setLeft(Operand left) { this.left = left; }

    public Operand getRight() { return right; }
    public void setRight(Operand right) { this.right = right; }

    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }

    public String getResultRadix() { return resultRadix; }
    public void setResultRadix(String resultRadix) { this.resultRadix = resultRadix; }

    public static class Operand {
        @NotBlank
        private String value;

        @NotBlank
        private String radix;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getRadix() { return radix; }
        public void setRadix(String radix) { this.radix = radix; }
    }
}
