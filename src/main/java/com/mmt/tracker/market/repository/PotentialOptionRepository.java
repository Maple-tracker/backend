package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.PotentialGrade;
import com.mmt.tracker.market.domain.PotentialOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PotentialOptionRepository extends JpaRepository<PotentialOption, Long> {
    Optional<PotentialOption> findByGradeAndStatPercentAndPotentialItal(
            PotentialGrade grade,
            Short statPercent,
            Boolean potentialItal
    );
} 
