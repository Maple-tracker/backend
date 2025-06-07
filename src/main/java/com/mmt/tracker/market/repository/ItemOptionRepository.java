package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    Optional<ItemOption> findByItemNameAndItemSlotAndStarForceAndStatTypeAndPotentialOptionAndAdditionalPotentialOptionAndStarforceScrollFlagAndEnchantedFlag(
            ItemName itemName,
            ItemSlot itemSlot,
            Short starForce,
            StatType statType,
            PotentialOption potentialOption,
            AdditionalPotentialOption additionalPotentialOption,
            Boolean starforceScrollFlag,
            Boolean enchantedFlag
    );

    List<ItemOption> findByItemName(ItemName itemName);
} 
