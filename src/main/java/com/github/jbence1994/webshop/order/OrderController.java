package com.github.jbence1994.webshop.order;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
@AllArgsConstructor
public class OrderController {
    private final OrderQueryService orderQueryService;
    private final OrderMapper orderMapper;

    @GetMapping
    public List<OrderDto> getOrders() {
        return orderQueryService.getOrders().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable("id") Long id) {
        var order = orderQueryService.getOrder(id);

        return orderMapper.toDto(order);
    }
}
