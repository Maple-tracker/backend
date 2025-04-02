package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.PotentialOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PotentialOptionRepository extends JpaRepository<PotentialOption, Long> {
    PotentialOption findByGradeAndStatPercentAndPotentialItal(
            String grade, Short statPercent, Boolean potentialItal);
} 
