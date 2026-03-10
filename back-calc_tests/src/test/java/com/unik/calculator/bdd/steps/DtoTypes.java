package com.unik.calculator.bdd.steps;

import io.cucumber.java.ParameterType;

import java.util.ArrayList;
import java.util.List;

public class DtoTypes {

    public record Operand(String value, String radix) {
    }

    // ВАЖНО: матчим только содержимое в кавычках, чтобы не прилетало HEX"
    @ParameterType("\"([^\"]*)\"")
    public List<Operand> operands(String raw) {
        // формат: 101|BIN;A|HEX
        if (raw == null || raw.isBlank())
            return List.of();

        String[] parts = raw.split(";");
        List<Operand> out = new ArrayList<>();

        for (String p : parts) {
            String[] kv = p.split("\\|");
            if (kv.length != 2)
                throw new IllegalArgumentException("bad operands token: " + p);

            String value = kv[0].trim();
            String radix = kv[1].trim();

            out.add(new Operand(value, radix));
        }
        return out;
    }
}