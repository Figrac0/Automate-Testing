package com.unik.calculator.core;

public enum Operation {
    ADD("+"), SUB("-"), MUL("*"), DIV("/");

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public static Operation fromToken(String token) {
        if (token == null) throw new IllegalArgumentException("op is null");
        return switch (token.trim()) {
            case "+" -> ADD;
            case "-" -> SUB;
            case "*" -> MUL;
            case "/" -> DIV;
            default -> throw new IllegalArgumentException("unsupported operation: " + token);
        };
    }
}
