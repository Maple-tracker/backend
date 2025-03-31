package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalPotentialOptionRepository extends JpaRepository<AdditionalPotentialOption, Long> {
    AdditionalPotentialOption findByGradeAndLinesAndPercentLines(
            String grade, Short lines, Short percentLines);
} 