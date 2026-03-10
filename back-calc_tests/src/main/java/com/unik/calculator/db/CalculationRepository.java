package com.unik.calculator.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CalculationRepository
        extends JpaRepository<CalculationEntity, Long>,
                JpaSpecificationExecutor<CalculationEntity> {
}
