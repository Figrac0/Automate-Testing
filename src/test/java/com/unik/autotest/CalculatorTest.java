package com.unik.autotest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    private final Calculator calc = new Calculator();

    @Nested
    @DisplayName("Addition tests")
    class AdditionTests {

        @ParameterizedTest(name = "{0}: {1} + {2} = {3}")
        @CsvSource({
                "BIN,1,1,10",
                "OCT,7,1,10",
                "DEC,2,3,5",
                "HEX,A,5,F"
        })
        void test_add_csvSource(Radix radix, String a, String b, String expected) {
            String result = calc.eval(radix, a, "+", b);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "From file {0} {1}+{3}={4}")
        @CsvFileSource(resources = "/testdata/add_cases.csv", numLinesToSkip = 1)
        void test_add_fromFile(String radixToken, String left, String op, String right, String expected) {
            Radix radix = Radix.fromToken(radixToken);
            String result = calc.eval(radix, left, op, right);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Subtraction tests")
    class SubtractionTests {

        @ParameterizedTest(name = "{0}: {1} - {2} = {3}")
        @CsvSource({
                "BIN,10,1,1",
                "OCT,10,1,7",
                "DEC,10,7,3",
                "HEX,10,A,6"
        })
        void test_sub(Radix radix, String a, String b, String expected) {
            String result = calc.eval(radix, a, "-", b);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Multiplication tests")
    class MultiplicationTests {

        @ParameterizedTest(name = "{0}: {1} * {2} = {3}")
        @CsvSource({
                "BIN,11,10,110",
                "OCT,7,2,16",
                "DEC,6,7,42",
                "HEX,A,2,14"
        })
        void test_mul(Radix radix, String a, String b, String expected) {
            String result = calc.eval(radix, a, "*", b);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Division tests")
    class DivisionTests {

        @ParameterizedTest(name = "{0}: {1} / {2} = {3}")
        @CsvSource({
                "BIN,100,10,10",
                "OCT,10,2,4",
                "DEC,21,7,3",
                "HEX,14,2,A"
        })
        void test_div(Radix radix, String a, String b, String expected) {
            String result = calc.eval(radix, a, "/", b);
            assertEquals(expected, result);
        }

        @Test
        void division_by_zero_should_throw_in_all_radixes() {
            assertThrows(ArithmeticException.class, () -> calc.eval(Radix.DEC, "10", "/", "0"));
            assertThrows(ArithmeticException.class, () -> calc.eval(Radix.BIN, "1010", "/", "0"));
            assertThrows(ArithmeticException.class, () -> calc.eval(Radix.OCT, "77", "/", "0"));
            assertThrows(ArithmeticException.class, () -> calc.eval(Radix.HEX, "FF", "/", "0"));
        }
    }

    @TestFactory
    @DisplayName("Dynamic tests from CSV for addition")
    Stream<DynamicTest> dynamic_add_tests() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/testdata/add_cases.csv"),
                        StandardCharsets.UTF_8))) {

            List<String> lines = reader.lines().skip(1).toList();

            return lines.stream().map(line -> {
                String[] parts = line.split(",");
                String radixToken = parts[0].trim();
                String left = parts[1].trim();
                String op = parts[2].trim();
                String right = parts[3].trim();
                String expected = parts[4].trim();

                Radix radix = Radix.fromToken(radixToken);

                String name = "Сложение – " + radix + " – " + left + " + " + right + " = " + expected;

                return DynamicTest.dynamicTest(name, () -> {
                    String result = calc.eval(radix, left, op, right);
                    assertEquals(expected, result);
                });
            });
        }
    }
}