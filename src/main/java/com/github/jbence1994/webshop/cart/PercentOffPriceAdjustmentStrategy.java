package com.github.jbence1994.webshop.cart;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentOffPriceAdjustmentStrategy implements PriceAdjustmentStrategy {

    @Override
    public Price adjustPrice(BigDecimal totalPrice, BigDecimal discountValue) {
        var discount = totalPrice
                .multiply(discountValue)
                .setScale(2, RoundingMode.DOWN);
        var discountedTotalPrice = totalPrice.subtract(discount);
        var discountAmount = totalPrice.subtract(discountedTotalPrice);

        return Price.withShippingCost(discountedTotalPrice, discountAmount);
    }
}
