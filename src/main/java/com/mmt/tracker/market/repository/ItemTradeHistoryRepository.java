package com.mmt.tracker.market.repository;

import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.ItemTradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTradeHistoryRepository extends JpaRepository<ItemTradeHistory, Long> {
    List<ItemTradeHistory> findByItemOption(ItemOption itemOption);
} 
