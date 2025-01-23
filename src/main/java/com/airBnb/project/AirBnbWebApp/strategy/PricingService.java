package com.airBnb.project.AirBnbWebApp.strategy;


import com.airBnb.project.AirBnbWebApp.entity.Inventory;
import com.airBnb.project.AirBnbWebApp.entity.Room;
import com.airBnb.project.AirBnbWebApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {
    private final InventoryRepository inventoryRepository;


    public BigDecimal calculateDynamicPricing(Inventory inventory){

        PricingStrategy pricingStrategy = new BasePricingStrategy();

        // apply the additional strategies
        pricingStrategy = new SurgePricingStrategy(pricingStrategy);
        pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy =  new UrgencyPricingStrategy(pricingStrategy);
        pricingStrategy = new HolidayPricingStrategy(pricingStrategy);

        return pricingStrategy.calculatePrice(inventory);
    }
    // Helper method to calculate the average price of a room
//    Returns null if room is not available for the request
//    public BigDecimal calculateAvgDynamicPrice(Room room, LocalDate startDate, LocalDate endDate, int numberOfRooms) {
//        List<Inventory> inventoryList = inventoryRepository.findAvailableInventory(room.getId(),
//                startDate, endDate, numberOfRooms);
//
//        if (inventoryList.size() != ChronoUnit.DAYS.between(startDate, endDate) + 1) {
//            return null;
//        }
//
//        BigDecimal totalPrice = inventoryList.stream()
//                .map(this::calculateDynamicPricing)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        return totalPrice.divide(BigDecimal.valueOf(inventoryList.size()), 2, RoundingMode.HALF_UP);
//    }
    public BigDecimal calculateTotalPrice(List<Inventory> inventoryList) {
        return inventoryList.stream()
                .map(this::calculateDynamicPricing)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
