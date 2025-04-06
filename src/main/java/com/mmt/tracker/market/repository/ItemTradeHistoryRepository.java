package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.ItemTradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTradeHistoryRepository extends JpaRepository<ItemTradeHistory, Long> {
    List<ItemTradeHistory> findByItemOption(ItemOption itemOption);
} 
