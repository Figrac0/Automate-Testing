package com.unik.calculator.bdd.hooks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unik.calculator.db.CalculationEntity;
import com.unik.calculator.db.CalculationRepository;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;
import java.util.List;

public class DbHooks {

    @Autowired
    private CalculationRepository repo;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbc;

    private static final String BASELINE_PATH = "fixtures/input_calculations.json";

    private byte[] baselineBytes;

    @After(order = 0)
    public void restoreBaselineAfterScenario() throws Exception {
        if (baselineBytes == null) {
            baselineBytes = readResourceBytesFromClasspath(BASELINE_PATH);
        }

        List<CalculationEntity> baseline = readEntitiesFromBytes(baselineBytes);

        truncateCalculations();
        repo.saveAll(baseline);
    }

    private void truncateCalculations() {
        jdbc.execute("TRUNCATE TABLE calculations RESTART IDENTITY CASCADE");
    }

    private List<CalculationEntity> readEntitiesFromBytes(byte[] bytes) throws Exception {
        byte[] clean = stripUtf8Bom(bytes);
        return mapper.readValue(clean, new TypeReference<List<CalculationEntity>>() {
        });
    }

    private byte[] readResourceBytesFromClasspath(String path) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream is = cl.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Classpath resource not found: " + path);
            }
            return is.readAllBytes();
        }
    }

    private byte[] stripUtf8Bom(byte[] bytes) {
        if (bytes == null || bytes.length < 3)
            return bytes;

        int b0 = bytes[0] & 0xFF;
        int b1 = bytes[1] & 0xFF;
        int b2 = bytes[2] & 0xFF;

        if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
            byte[] out = new byte[bytes.length - 3];
            System.arraycopy(bytes, 3, out, 0, out.length);
            return out;
        }
        return bytes;
    }
}