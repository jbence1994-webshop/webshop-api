package com.github.jbence1994.webshop.checkout;

import com.github.jbence1994.webshop.auth.AuthService;
import com.github.jbence1994.webshop.cart.CartQueryService;
import com.github.jbence1994.webshop.cart.EmptyCartException;
import com.github.jbence1994.webshop.coupon.CouponService;
import com.github.jbence1994.webshop.order.Order;
import com.github.jbence1994.webshop.order.OrderService;
import com.github.jbence1994.webshop.order.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartQueryService cartQueryService;
    private final CouponService couponService;
    private final OrderService orderService;
    private final AuthService authService;

    @Override
    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cartId = request.getCartId();

        var cart = cartQueryService.getCart(cartId);

        if (cart.isEmpty()) {
            throw new EmptyCartException(cartId);
        }

        var user = authService.getCurrentUser();

        var order = Order.from(cart);
        order.setCustomer(user);
        orderService.createOrder(order);

        if (cart.hasCouponApplied()) {
            couponService.redeemCoupon(
                    user.getId(),
                    cart.getCouponCode(),
                    order.getId()
            );
        }

        cart.clear();

        var earnedLoyaltyPoints = order.calculateLoyaltyPoints();
        user.earnLoyaltyPoints(earnedLoyaltyPoints);
        order.setLoyaltyPoints(earnedLoyaltyPoints);

        // TODO: Payment integration.

        // 1) If payment was successful:
        order.setStatus(OrderStatus.COMPLETED);

        //2) If payment was failed:
        // order.setStatus(PaymentStatus.FAILED);

        // 3) If payment aborted for a long time:
        // order.setStatus(PaymentStatus.CANCELLED);

        return new CheckoutResponse(order.getId());
    }
}
