package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.PotentialOption;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    Optional<ItemOption> findByItemNameAndItemSlotAndStarForceAndStatTypeAndPotentialOptionAndAdditionalPotentialOptionAndStarforceScrollFlagAndEnchantedFlag(
            String itemName,
            String itemSlot,
            Short starForce,
            String statType, 
            PotentialOption potentialOption,
            AdditionalPotentialOption additionalPotentialOption,
            Boolean starforceScrollFlag,
            Boolean enchantedFlag
    );

    List<ItemOption> findByItemName(String itemName);
} 
