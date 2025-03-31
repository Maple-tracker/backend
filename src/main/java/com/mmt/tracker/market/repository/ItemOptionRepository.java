package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.PotentialOption;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    ItemOption findByItemNameAndItemSlotAndStarForceAndStatTypeAndPotentialOptionAndAdditionalPotentialOptionAndStarforceScrollFlagAndEnchantedFlag(
            String itemName,
            String itemSlot,
            Short starForce,
            String statType, 
            PotentialOption potentialOption,
            AdditionalPotentialOption additionalPotentialOption,
            Boolean starforceScrollFlag,
            Boolean enchantedFlag
    );
} 