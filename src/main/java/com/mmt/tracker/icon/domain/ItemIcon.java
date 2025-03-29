package com.mmt.tracker.icon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_icons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemIcon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String itemName;

    @Column(nullable = false)
    private String iconUrl;

    @Builder
    public ItemIcon(String itemName, String iconUrl) {
        this.itemName = itemName;
        this.iconUrl = iconUrl;
    }
}
