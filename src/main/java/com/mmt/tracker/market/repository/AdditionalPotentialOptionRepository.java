package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.PotentialGrade;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdditionalPotentialOptionRepository extends JpaRepository<AdditionalPotentialOption, Long> {
    Optional<AdditionalPotentialOption> findByGradeAndLinesAndPercentLines(
            PotentialGrade grade,
            Short lines,
            Short percentLines
    );
} 
