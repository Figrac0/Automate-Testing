package com.unik.calculator.core;

public enum Radix {
    BIN(2), OCT(8), DEC(10), HEX(16);

    private final int base;

    Radix(int base) {
        this.base = base;
    }

    public int base() {
        return base;
    }

    public static Radix fromToken(String token) {
        if (token == null) throw new IllegalArgumentException("radix is null");
        String t = token.trim().toUpperCase();
        return switch (t) {
            case "2", "BIN", "BINARY" -> BIN;
            case "8", "OCT", "OCTAL" -> OCT;
            case "10", "DEC", "DECIMAL" -> DEC;
            case "16", "HEX", "HEXADECIMAL" -> HEX;
            default -> throw new IllegalArgumentException("unsupported radix: " + token);
        };
    }
}
