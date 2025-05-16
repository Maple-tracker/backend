package com.mmt.tracker.market.init;

import com.mmt.tracker.market.domain.PotentialGrade;
import com.mmt.tracker.market.domain.PotentialOption;
import com.mmt.tracker.market.domain.AdditionalPotentialOption;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.domain.ItemSlot;
import com.mmt.tracker.market.domain.StatType;
import com.mmt.tracker.market.domain.ItemOption;
import com.mmt.tracker.market.domain.ItemTradeHistory;
import com.mmt.tracker.market.repository.PotentialOptionRepository;
import com.mmt.tracker.market.repository.AdditionalPotentialOptionRepository;
import com.mmt.tracker.market.repository.ItemOptionRepository;
import com.mmt.tracker.market.repository.ItemTradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class DataLoader {

    private final PotentialOptionRepository potentialOptionRepository;
    private final AdditionalPotentialOptionRepository additionalPotentialOptionRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ItemTradeHistoryRepository itemTradeHistoryRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            log.info("CSV 데이터 로딩을 시작합니다.");
            
            Map<String, PotentialOption> potentialOptionCache = new HashMap<>();
            Map<String, AdditionalPotentialOption> additionalPotentialOptionCache = new HashMap<>();
            Map<String, ItemOption> itemOptionCache = new HashMap<>();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new ClassPathResource("auction_data.csv").getInputStream(), StandardCharsets.UTF_8))) {
                
                // Skip header
                String header = reader.readLine();
                if (header == null) {
                    log.error("CSV 파일이 비어있습니다.");
                    return;
                }
                
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    try {
                        String[] data = line.split(",");
                        if (data.length != 14) {
                            log.warn("잘못된 데이터 형식 (라인 {}): {}", lineNumber, line);
                            continue;
                        }

                        // 아이템 이름 변환
                        ItemName itemName = ItemName.fromString(data[0]);
                        
                        // 잠재능력 옵션 생성 또는 조회
                        String potentialKey = String.format("%s_%s_%s", data[5], data[6], data[7]);
                        potentialOptionCache.computeIfAbsent(potentialKey, k -> {
                            PotentialOption option = new PotentialOption(
                                    getPotentialGradeByValue(data[5]),
                                    Short.parseShort(data[6]),
                                    Integer.parseInt(data[7]) == 1
                            );
                            return potentialOptionRepository.save(option);
                        });

                        // 에디셔널 잠재능력 옵션 생성 또는 조회
                        String additionalKey = String.format("%s_%s_%s", data[8], data[9], data[10]);
                        additionalPotentialOptionCache.computeIfAbsent(additionalKey, k -> {
                            AdditionalPotentialOption option = new AdditionalPotentialOption(
                                    getPotentialGradeByValue(data[8]),
                                    Short.parseShort(data[9]),
                                    Short.parseShort(data[10])
                            );
                            return additionalPotentialOptionRepository.save(option);
                        });

                        // 아이템 옵션 생성 또는 조회
                        String itemOptionKey = String.format("%s_%s_%s_%s_%s_%s_%s",
                                itemName,
                                data[1], // starForce
                                data[4], // statType
                                potentialKey,
                                additionalKey,
                                data[12], // starforceScrollFlag
                                data[13]  // enchantedFlag
                        );
                        
                        ItemOption itemOption = itemOptionCache.computeIfAbsent(itemOptionKey, k -> {
                            ItemOption option = new ItemOption(
                                    itemName,
                                    getItemSlotFromItemName(itemName),
                                    Short.parseShort(data[1]),
                                    getStatTypeByValue(convertStatType(data[4])),
                                    potentialOptionCache.get(potentialKey),
                                    additionalPotentialOptionCache.get(additionalKey),
                                    Integer.parseInt(data[12]) == 1,
                                    Integer.parseInt(data[13]) == 1
                            );
                            return itemOptionRepository.save(option);
                        });

                        // 시세 데이터 저장
                        LocalDate date = LocalDate.parse(data[2], DateTimeFormatter.ISO_DATE);
                        LocalDateTime timeStamp = LocalDateTime.of(date, LocalTime.MIDNIGHT);
                        Long amount = Long.parseLong(data[3]);
                        Short cuttableCount = Short.parseShort(data[11]);

                        ItemTradeHistory tradeHistory = new ItemTradeHistory(
                                itemOption,
                                amount,
                                timeStamp,
                                cuttableCount
                        );
                        itemTradeHistoryRepository.save(tradeHistory);
                    } catch (Exception e) {
                        log.error("데이터 처리 중 오류 발생 (라인 {}): {}", lineNumber, e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.error("CSV 파일 읽기 중 오류 발생: {}", e.getMessage());
            }
            
            log.info("CSV 데이터 로딩이 완료되었습니다.");
        };
    }

    private ItemSlot getItemSlotFromItemName(ItemName itemName) {
        String name = itemName.name();
        if (name.startsWith("EYE_")) return ItemSlot.EYE_ECC;
        if (name.startsWith("FACE_")) return ItemSlot.FACE_ECC;
        if (name.startsWith("PENDANT_")) return ItemSlot.PENDANT;
        if (name.startsWith("BELT_")) return ItemSlot.BELT;
        if (name.startsWith("RING_")) return ItemSlot.RING;
        if (name.startsWith("EARRING_")) return ItemSlot.EARRINGS;
        throw new IllegalArgumentException("Unknown item slot for item: " + itemName);
    }

    private PotentialGrade getPotentialGradeByValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return PotentialGrade.NONE;
        }
        return Arrays.stream(PotentialGrade.values())
                .filter(grade -> grade.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown potential grade: " + value));
    }

    private StatType getStatTypeByValue(String value) {
        return Arrays.stream(StatType.values())
                .filter(type -> type.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown stat type: " + value));
    }

    private String convertStatType(String value) {
        if (value.equals("ALL")) {
            return "올스탯";
        }
        return value;
    }
} 
