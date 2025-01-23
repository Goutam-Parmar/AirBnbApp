package com.airBnb.project.AirBnbWebApp.strategy;

import com.airBnb.project.AirBnbWebApp.entity.Inventory;


import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
