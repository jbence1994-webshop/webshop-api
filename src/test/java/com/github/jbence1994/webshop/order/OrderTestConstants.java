package com.github.jbence1994.webshop.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderTestConstants {
    BigDecimal TOTAL_PRICE = BigDecimal.valueOf(49.99);
    BigDecimal DISCOUNT_AMOUNT = BigDecimal.ZERO;
    BigDecimal SHIPPING_COST = BigDecimal.valueOf(20);
    int EARNED_LOYALTY_POINTS = 2;
    BigDecimal SUB_TOTAL = TOTAL_PRICE;
    LocalDateTime CREATED_AT = LocalDateTime.of(2025, 6, 18, 16, 18, 59);
}
