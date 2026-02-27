package com.unik.autotest;

import java.math.BigInteger;

public final class Calculator {

    public BigInteger parse(String value, Radix radix) {
        try {
            return new BigInteger(value.trim(), radix.base());
        } catch (Exception e) {
            throw new IllegalArgumentException("cannot parse '" + value + "' as base " + radix.base());
        }
    }

    public String format(BigInteger value, Radix radix) {
        return value.toString(radix.base()).toUpperCase();
    }

    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    public BigInteger sub(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    public BigInteger mul(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    public BigInteger div(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO))
            throw new ArithmeticException("Division by zero");
        return a.divide(b);
    }

    public String eval(Radix radix, String left, String op, String right) {
        BigInteger a = parse(left, radix);
        BigInteger b = parse(right, radix);

        BigInteger res = switch (op.trim()) {
            case "+" -> add(a, b);
            case "-" -> sub(a, b);
            case "*" -> mul(a, b);
            case "/" -> div(a, b);
            default -> throw new IllegalArgumentException("unsupported operation: " + op);
        };

        return format(res, radix);
    }
}