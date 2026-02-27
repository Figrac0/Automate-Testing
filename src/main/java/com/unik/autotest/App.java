package com.unik.autotest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class App {

    public static void main(String[] args) throws Exception {
        Calculator calc = new Calculator();

        System.out.println("Calculator");
        System.out.println("Format: <radix> <a> <op> <b>");
        System.out.println("Radix: 2|8|10|16 or BIN|OCT|DEC|HEX");
        System.out.println("Ops: + - * /");
        System.out.println("exit - quit");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = br.readLine();
                if (line == null)
                    return;

                String t = line.trim();
                if (t.equalsIgnoreCase("exit"))
                    return;
                if (t.isEmpty())
                    continue;

                try {
                    String[] parts = t.split("\\s+");
                    if (parts.length != 4) {
                        System.out.println("Error: expected 4 tokens");
                        continue;
                    }

                    Radix radix = Radix.fromToken(parts[0]);
                    String result = calc.eval(radix, parts[1], parts[2], parts[3]);
                    System.out.println(result);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}