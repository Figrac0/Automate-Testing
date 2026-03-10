package com.unik.calculator.api;

import com.unik.calculator.api.dto.CalcRequest;
import com.unik.calculator.api.dto.CalcResponse;
import com.unik.calculator.api.dto.CalculationDto;
import com.unik.calculator.core.Calculator;
import com.unik.calculator.core.Operation;
import com.unik.calculator.core.Radix;
import com.unik.calculator.db.CalculationEntity;
import com.unik.calculator.db.CalculationRepository;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CalculatorController {

        private final Calculator calc = new Calculator();
        private final CalculationRepository repo;

        public CalculatorController(CalculationRepository repo) {
                this.repo = repo;
        }

        @PostMapping("/calc")
        public CalcResponse calculate(@RequestBody @Valid CalcRequest req) {
                Radix leftRadix = Radix.fromToken(req.getLeft().getRadix());
                Radix rightRadix = Radix.fromToken(req.getRight().getRadix());
                Radix resultRadix = Radix.fromToken(req.getResultRadix());
                Operation op = Operation.fromToken(req.getOp());

                String result = calc.eval(
                                leftRadix, req.getLeft().getValue(),
                                op,
                                rightRadix, req.getRight().getValue(),
                                resultRadix);

                CalculationEntity e = new CalculationEntity();
                e.setLeftValue(req.getLeft().getValue());
                e.setLeftRadix(leftRadix.name());
                e.setRightValue(req.getRight().getValue());
                e.setRightRadix(rightRadix.name());
                e.setOperation(op.name());
                e.setResultValue(result);
                e.setResultRadix(resultRadix.name());

                CalculationEntity saved = repo.save(e);

                return new CalcResponse(
                                saved.getId(),
                                saved.getResultValue(),
                                saved.getResultRadix(),
                                saved.getCreatedAt());
        }

        @GetMapping("/calculations")
        public List<CalculationDto> search(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
                        @RequestParam(required = false) String operation,
                        @RequestParam(required = false) String leftRadix,
                        @RequestParam(required = false) String rightRadix) {
                Specification<CalculationEntity> spec = (root, query, cb) -> cb.between(root.get("createdAt"), from,
                                to);

                if (operation != null && !operation.isBlank()) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("operation"),
                                        operation.trim().toUpperCase()));
                }
                if (leftRadix != null && !leftRadix.isBlank()) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("leftRadix"),
                                        leftRadix.trim().toUpperCase()));
                }
                if (rightRadix != null && !rightRadix.isBlank()) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("rightRadix"),
                                        rightRadix.trim().toUpperCase()));
                }

                return repo.findAll(spec).stream()
                                .map(e -> new CalculationDto(
                                                e.getId(),
                                                e.getLeftValue(), e.getLeftRadix(),
                                                e.getRightValue(), e.getRightRadix(),
                                                e.getOperation(),
                                                e.getResultValue(), e.getResultRadix(),
                                                e.getCreatedAt()))
                                .toList();
        }
}