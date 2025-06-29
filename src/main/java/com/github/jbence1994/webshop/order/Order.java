package com.github.jbence1994.webshop.order;

import com.github.jbence1994.webshop.cart.Cart;
import com.github.jbence1994.webshop.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratedColumn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    private BigDecimal totalPrice;

    private BigDecimal discountAmount;

    private BigDecimal shippingCost;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int loyaltyPoints;

    @Column(insertable = false, updatable = false)
    @GeneratedColumn("created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public boolean isPlacedBy(String customerEmail) {
        return customerEmail.equals(customer.getEmail());
    }

    public static Order from(Cart cart) {
        var price = cart.calculateTotal();

        var order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(price.getTotalPrice());
        order.setDiscountAmount(price.getDiscountAmount());
        order.setShippingCost(price.getShippingCost());

        var items = cart.mapCartItemsToOrderItems();

        items.forEach(item -> {
                    item.setOrder(order);
                    order.items.add(item);
                }
        );

        return order;
    }

    public int calculateLoyaltyPoints() {
        // FIXME: Extract this value from here.
        //  Earning values can be change in timely campaigns.
        final int PER_EVERY_TWENTY_DOLLARS = 20;

        return totalPrice
                .divide(BigDecimal.valueOf(PER_EVERY_TWENTY_DOLLARS), RoundingMode.DOWN)
                .setScale(0, RoundingMode.DOWN)
                .intValue();
    }
}
