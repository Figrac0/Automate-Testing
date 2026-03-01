package com.unik.calculator.core;

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

    public BigInteger add(BigInteger a, BigInteger b) { return a.add(b); }
    public BigInteger sub(BigInteger a, BigInteger b) { return a.subtract(b); }
    public BigInteger mul(BigInteger a, BigInteger b) { return a.multiply(b); }

    public BigInteger div(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("Division by zero");
        return a.divide(b);
    }

    public String eval(
        Radix leftRadix, String left,
        Operation op,
        Radix rightRadix, String right,
        Radix resultRadix
    ) {
        BigInteger a = parse(left, leftRadix);
        BigInteger b = parse(right, rightRadix);

        BigInteger res = switch (op) {
            case ADD -> add(a, b);
            case SUB -> sub(a, b);
            case MUL -> mul(a, b);
            case DIV -> div(a, b);
        };

        return format(res, resultRadix);
    }
}
