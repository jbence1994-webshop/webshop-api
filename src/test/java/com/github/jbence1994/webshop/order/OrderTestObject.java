package com.github.jbence1994.webshop.order;

import java.util.List;

import static com.github.jbence1994.webshop.order.OrderItemTestObject.orderItem;
import static com.github.jbence1994.webshop.order.OrderTestConstants.CREATED_AT;
import static com.github.jbence1994.webshop.order.OrderTestConstants.TOTAL_PRICE;
import static com.github.jbence1994.webshop.user.UserTestObject.user;

public final class OrderTestObject {
    public static Order order() {
        return new Order(
                1L,
                user(),
                TOTAL_PRICE,
                PaymentStatus.COMPLETED,
                CREATED_AT,
                List.of(orderItem())
        );
    }
}
