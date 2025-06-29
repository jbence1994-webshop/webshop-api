package com.github.jbence1994.webshop.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Price {
    private BigDecimal totalPrice;
    private BigDecimal discountAmount;
    private BigDecimal shippingCost;

    public static Price withShippingCost(BigDecimal totalPrice) {
        return new Price(totalPrice, BigDecimal.ZERO, BigDecimal.valueOf(20.00));
    }

    public static Price withShippingCost(BigDecimal totalPrice, BigDecimal discountAmount) {
        return new Price(totalPrice, discountAmount, BigDecimal.valueOf(20.00));
    }

    public static Price withFreeShipping(BigDecimal totalPrice) {
        return new Price(totalPrice, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
