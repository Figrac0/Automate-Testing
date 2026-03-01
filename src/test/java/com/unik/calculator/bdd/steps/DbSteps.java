package com.unik.calculator.bdd.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unik.calculator.db.CalculationEntity;
import com.unik.calculator.db.CalculationRepository;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;
import java.util.List;

public class DbSteps {

    @Autowired
    CalculationRepository repo;

    @Autowired
    ObjectMapper om;

    @Autowired
    JdbcTemplate jdbc;

    @Given("database seeded from {string}")
    public void databaseSeededFrom(String classpathResource) throws Exception {
        byte[] bytes = readResourceBytes("/" + classpathResource);
        byte[] clean = stripUtf8Bom(bytes);

        List<CalculationEntity> entities = om.readValue(clean, new TypeReference<List<CalculationEntity>>() {
        });

        truncateCalculations();
        repo.saveAll(entities);
    }

    private void truncateCalculations() {
        jdbc.execute("TRUNCATE TABLE calculations RESTART IDENTITY CASCADE");
    }

    private byte[] readResourceBytes(String absoluteClasspathPath) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(absoluteClasspathPath)) {
            if (is == null) {
                throw new IllegalArgumentException("Classpath resource not found: " + absoluteClasspathPath);
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