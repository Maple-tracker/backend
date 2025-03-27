package com.mmt.tracker.icon.repository;

import com.mmt.tracker.icon.domain.ItemIcon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemIconRepository extends JpaRepository<ItemIcon, Long> {
    
    Optional<ItemIcon> findByItemName(String itemName);
} 