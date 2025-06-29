package com.github.jbence1994.webshop.cart;

import com.github.jbence1994.webshop.coupon.Coupon;
import com.github.jbence1994.webshop.coupon.DiscountType;
import com.github.jbence1994.webshop.order.OrderItem;
import com.github.jbence1994.webshop.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(insertable = false, updatable = false)
    @GeneratedColumn("created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "applied_coupon")
    private Coupon appliedCoupon;

    public CartItem getItem(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product) {
        var cartItem = getItem(product.getId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);

            items.add(cartItem);
        }

        return cartItem;
    }

    public void removeItem(Long productId) {
        var cartItem = getItem(productId);

        if (cartItem != null) {
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public boolean hasCouponApplied() {
        return appliedCoupon != null;
    }

    public String getCouponCode() {
        return appliedCoupon.getCode();
    }

    public void clear() {
        appliedCoupon = null;
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Price calculateTotal() {
        var totalPrice = items.stream()
                .map(CartItem::calculateSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!hasCouponApplied()) {
            return Price.withShippingCost(totalPrice);
        }

        if (DiscountType.FREE_SHIPPING.equals(appliedCoupon.getType())) {
            return Price.withFreeShipping(totalPrice);
        }

        return PriceAdjustmentStrategyFactory
                .getPriceAdjustmentStrategy(appliedCoupon.getType())
                .adjustPrice(totalPrice, appliedCoupon.getValue());
    }

    public List<OrderItem> mapCartItemsToOrderItems() {
        var orderItems = new ArrayList<OrderItem>();

        items.forEach(item -> {
            var orderItem = new OrderItem(item.getProduct(), item.getQuantity());
            orderItems.add(orderItem);
        });

        return orderItems;
    }
}
