package com.unik.calculator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unik.calculator.api.dto.CalcRequest;
import com.unik.calculator.db.CalculationEntity;
import com.unik.calculator.db.CalculationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CalculatorIT {

    @SuppressWarnings("resource")
    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("lab2_calc")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.flyway.enabled", () -> true);
        r.add("spring.flyway.locations", () -> "classpath:db/migration");
    }

    @LocalServerPort
    int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    private final CalculationRepository repo;
    private final ObjectMapper om;

    CalculatorIT(CalculationRepository repo, ObjectMapper om) {
        this.repo = repo;
        this.om = om;
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @BeforeEach
    void seed() throws Exception {
        assertTrue(postgres.isRunning());

        repo.deleteAll();

        String inputJson = new String(
                getClass().getResourceAsStream("/fixtures/input_calculations.json").readAllBytes(),
                StandardCharsets.UTF_8);
        List<CalculationEntity> entities = om.readValue(inputJson, new TypeReference<>() {
        });
        repo.saveAll(entities);
    }

    @Test
    void post_calc_should_return_result_and_persist() {
        CalcRequest req = new CalcRequest();
        CalcRequest.Operand left = new CalcRequest.Operand();
        left.setValue("101");
        left.setRadix("BIN");
        req.setLeft(left);

        CalcRequest.Operand right = new CalcRequest.Operand();
        right.setValue("A");
        right.setRadix("HEX");
        req.setRight(right);

        req.setOp("+");
        req.setResultRadix("DEC");

        ResponseEntity<Map> resp = rest.postForEntity(baseUrl() + "/calc", req, Map.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("15", String.valueOf(resp.getBody().get("result")));
        assertEquals("DEC", String.valueOf(resp.getBody().get("resultRadix")));
        assertNotNull(resp.getBody().get("createdAt"));
    }

    @Test
    void get_calculations_should_filter_by_time_and_operation() throws Exception {
        String from = "2026-02-26T23:59:59Z";
        String to = "2026-02-27T00:00:01Z";
        String url = baseUrl() + "/calculations?from=" + from + "&to=" + to + "&operation=ADD";

        ResponseEntity<String> resp = rest.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        String json = resp.getBody();
        assertNotNull(json);

        List<Map<String, Object>> actual = om.readValue(json, new TypeReference<>() {
        });

        String expectedJson = new String(
                getClass().getResourceAsStream("/fixtures/expected_search_add.json").readAllBytes(),
                StandardCharsets.UTF_8);
        List<Map<String, Object>> expected = om.readValue(expectedJson, new TypeReference<>() {
        });

        assertEquals(1, actual.size());
        assertEquals(expected.get(0).get("operation"), actual.get(0).get("operation"));
        assertEquals(expected.get(0).get("leftRadix"), actual.get(0).get("leftRadix"));
        assertEquals(expected.get(0).get("rightRadix"), actual.get(0).get("rightRadix"));
        assertEquals(expected.get(0).get("resultValue"), actual.get(0).get("resultValue"));
        assertEquals(expected.get(0).get("resultRadix"), actual.get(0).get("resultRadix"));
    }
}
