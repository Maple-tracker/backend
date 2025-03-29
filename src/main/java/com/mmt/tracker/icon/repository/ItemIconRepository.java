package com.mmt.tracker.icon.repository;

import com.mmt.tracker.icon.domain.ItemIcon;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemIconRepository extends JpaRepository<ItemIcon, Long> {

    Optional<ItemIcon> findByItemName(String itemName);
}
