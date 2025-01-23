package com.airBnb.project.AirBnbWebApp.strategy;

import com.airBnb.project.AirBnbWebApp.entity.Inventory;


import java.math.BigDecimal;

public interface PricingStrategy {


    BigDecimal calculatePrice(Inventory inventory);
}
