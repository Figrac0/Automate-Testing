package com.unik.calculator.bdd.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TableClassSteps {

    @Autowired
    ApiSteps apiSteps;

    @When("I calculate using class table with op {string} expecting radix {string}")
    public void calcUsingClassTable(String op, String resultRadix, DataTable table) throws Exception {
        // таблица без заголовка, но маппим в класс вручную через rows
        // columns: value | radix | side
        List<List<String>> rows = table.asLists(String.class);

        DtoTypes.Operand left = null;
        DtoTypes.Operand right = null;

        for (List<String> r : rows) {
            String value = r.get(0);
            String radix = r.get(1);
            String side = r.get(2);
            if ("left".equalsIgnoreCase(side))
                left = new DtoTypes.Operand(value, radix);
            if ("right".equalsIgnoreCase(side))
                right = new DtoTypes.Operand(value, radix);
        }

        if (left == null || right == null)
            throw new IllegalArgumentException("left/right missing");
        apiSteps.calcSimple(left.value(), left.radix(), right.value(), right.radix(), op, resultRadix);
    }
}