package com.github.jbence1994.webshop.order;

import java.util.List;

import static com.github.jbence1994.webshop.order.OrderItemDtoTestObject.orderItemDto;
import static com.github.jbence1994.webshop.order.OrderTestConstants.CREATED_AT;
import static com.github.jbence1994.webshop.order.OrderTestConstants.TOTAL_PRICE;

public final class OrderDtoTestObject {
    public static OrderDto orderDto() {
        return new OrderDto(
                1L,
                TOTAL_PRICE,
                PaymentStatus.COMPLETED.name(),
                CREATED_AT,
                List.of(orderItemDto())
        );
    }
}
