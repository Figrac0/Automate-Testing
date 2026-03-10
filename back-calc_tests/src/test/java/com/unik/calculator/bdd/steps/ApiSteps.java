package com.unik.calculator.bdd.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unik.calculator.api.dto.CalcRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ApiSteps {

    @LocalServerPort
    int port;

    private final RestTemplate rest = new RestTemplate();

    @Autowired
    ObjectMapper om;

    private ResponseEntity<String> lastResponse;

    private String baseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @When("I calculate left {string} in {string} and right {string} in {string} with op {string} expecting radix {string}")
    public void calcSimple(String lv, String lr, String rv, String rr, String op, String resultRadix) throws Exception {
        CalcRequest req = new CalcRequest();

        CalcRequest.Operand left = new CalcRequest.Operand();
        left.setValue(lv);
        left.setRadix(lr);
        req.setLeft(left);

        CalcRequest.Operand right = new CalcRequest.Operand();
        right.setValue(rv);
        right.setRadix(rr);
        req.setRight(right);

        req.setOp(op);
        req.setResultRadix(resultRadix);

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);

        String body = om.writeValueAsString(req);

        try {
            lastResponse = rest.exchange(
                    baseUrl() + "/calc",
                    HttpMethod.POST,
                    new HttpEntity<>(body, h),
                    String.class);
        } catch (HttpStatusCodeException ex) {
            lastResponse = ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(ex.getResponseHeaders() != null ? ex.getResponseHeaders() : new HttpHeaders())
                    .body(ex.getResponseBodyAsString());
        }
    }

    @When("I search calculations from {string} to {string} with operation {string}")
    public void search(String from, String to, String operation) {
        String url = baseUrl() + "/calculations?from=" + from + "&to=" + to + "&operation=" + operation;
        try {
            lastResponse = rest.getForEntity(url, String.class);
        } catch (HttpStatusCodeException ex) {
            lastResponse = ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(ex.getResponseHeaders() != null ? ex.getResponseHeaders() : new HttpHeaders())
                    .body(ex.getResponseBodyAsString());
        }
    }

    @Then("response status is {int}")
    public void statusIs(int code) {
        assertNotNull(lastResponse);
        assertEquals(code, lastResponse.getStatusCode().value());
    }

    @Then("calc response has result {string} and radix {string}")
    public void calcResponseHas(String result, String radix) throws Exception {
        assertNotNull(lastResponse);
        Map<String, Object> m = om.readValue(lastResponse.getBody(), new TypeReference<>() {
        });
        assertEquals(result, String.valueOf(m.get("result")));
        assertEquals(radix, String.valueOf(m.get("resultRadix")));
        assertNotNull(m.get("createdAt"));
        assertNotNull(m.get("id"));
    }

    @Then("search response size is {int}")
    public void searchSizeIs(int size) throws Exception {
        assertNotNull(lastResponse);
        List<Map<String, Object>> list = om.readValue(lastResponse.getBody(), new TypeReference<>() {
        });
        assertEquals(size, list.size());
    }

    @Then("first item has operation {string} leftRadix {string} rightRadix {string}")
    public void firstHas(String op, String leftRadix, String rightRadix) throws Exception {
        assertNotNull(lastResponse);
        List<Map<String, Object>> list = om.readValue(lastResponse.getBody(), new TypeReference<>() {
        });
        assertFalse(list.isEmpty());
        Map<String, Object> first = list.get(0);

        assertEquals(op, String.valueOf(first.get("operation")));
        assertEquals(leftRadix, String.valueOf(first.get("leftRadix")));
        assertEquals(rightRadix, String.valueOf(first.get("rightRadix")));
    }

    @When("I calculate using table operands with op {string} expecting radix {string}")
    public void calcUsingTable(String op, String resultRadix, DataTable table) throws Exception {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        Map<String, String> leftRow = rows.stream()
                .filter(r -> "left".equalsIgnoreCase(r.get("side")))
                .findFirst().orElseThrow();

        Map<String, String> rightRow = rows.stream()
                .filter(r -> "right".equalsIgnoreCase(r.get("side")))
                .findFirst().orElseThrow();

        calcSimple(
                leftRow.get("value"), leftRow.get("radix"),
                rightRow.get("value"), rightRow.get("radix"),
                op,
                resultRadix);
    }

    @When("I calculate using class operands {operands} with op {string} expecting radix {string}")
    public void calcUsingCustomDelimiter(List<DtoTypes.Operand> ops, String op, String resultRadix) throws Exception {
        if (ops.size() != 2)
            throw new IllegalArgumentException("need 2 operands");
        DtoTypes.Operand left = ops.get(0);
        DtoTypes.Operand right = ops.get(1);
        calcSimple(left.value(), left.radix(), right.value(), right.radix(), op, resultRadix);
    }
}