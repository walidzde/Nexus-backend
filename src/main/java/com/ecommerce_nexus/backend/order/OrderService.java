package com.ecommerce_nexus.backend.order;

import com.ecommerce_nexus.backend.cart.Cart;
import com.ecommerce_nexus.backend.cart.CartItem;
import com.ecommerce_nexus.backend.cart.CartRepository;
import com.ecommerce_nexus.backend.order.dto.CheckoutRequest;
import com.ecommerce_nexus.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public Order placeOrderFromCart(CheckoutRequest request) {
        User user = resolveUserOrNull();
        Cart cart;
        if (user != null) {
            cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new IllegalStateException("Cart is empty"));
        } else {
            String guestToken = request.getGuestToken();
            if (guestToken == null || guestToken.isBlank()) {
                throw new IllegalArgumentException("guestToken is required for guest checkout");
            }
            cart = cartRepository.findByGuestToken(guestToken)
                    .orElseThrow(() -> new IllegalStateException("Cart is empty"));
        }
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user); // null for guest orders
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setShippingAddress(request.getShippingAddress());

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getProduct().getPrice());
            order.getItems().add(oi);
            total = total.add(ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        order.setTotal(total);
        Order saved = orderRepository.save(order);

        // Clear cart after order placed
        cart.getItems().clear();

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Order> getMyOrders() {
        User user = resolveUserOrNull();
        if (user == null) {
            throw new IllegalArgumentException("Authentication required");
        }
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public Order getMyOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        // ensure owner
        User user = resolveUserOrNull();
        if (user == null || order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied to this order");
        }
        return order;
    }

    private User resolveUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        return (principal instanceof User) ? (User) principal : null;
    }
}
