package com.ecommerce_nexus.backend.cart;

import com.ecommerce_nexus.backend.cart.dto.CartItemRequest;
import com.ecommerce_nexus.backend.cart.dto.CartItemResponse;
import com.ecommerce_nexus.backend.cart.dto.CartResponse;
import com.ecommerce_nexus.backend.product.Product;
import com.ecommerce_nexus.backend.product.ProductRepository;
import com.ecommerce_nexus.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartResponse getMyCart(String guestToken) {
        Cart cart = getOrCreateCart(resolveUserOrNull(), guestToken);
        return toResponse(cart);
    }

    public CartResponse addItem(CartItemRequest request, String guestToken) {
        User user = resolveUserOrNull();
        Cart cart = getOrCreateCart(user, guestToken);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + request.getProductId()));
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem ni = new CartItem();
                    ni.setCart(cart);
                    ni.setProduct(product);
                    ni.setQuantity(0);
                    cart.getItems().add(ni);
                    return ni;
                });
        item.setQuantity(item.getQuantity() + request.getQuantity());
        Cart saved = cartRepository.save(cart);
        return toResponse(saved);
    }

    public CartResponse updateItem(Long productId, int quantity, String guestToken) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        Cart cart = getOrCreateCart(resolveUserOrNull(), guestToken);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));
        item.setQuantity(quantity);
        return toResponse(cart);
    }

    public CartResponse removeItem(Long productId, String guestToken) {
        Cart cart = getOrCreateCart(resolveUserOrNull(), guestToken);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return toResponse(cart);
    }

    public void clearCart(String guestToken) {
        Cart cart = getOrCreateCart(resolveUserOrNull(), guestToken);
        cart.getItems().clear();
    }

    private Cart getOrCreateCart(User user, String guestToken) {
        if (user != null) {
            return cartRepository.findByUser(user).orElseGet(() -> {
                Cart c = new Cart();
                c.setUser(user);
                return cartRepository.save(c);
            });
        }
        // guest flow
        if (guestToken == null || guestToken.isBlank()) {
            throw new IllegalArgumentException("guestToken is required for guest cart operations");
        }
        return cartRepository.findByGuestToken(guestToken).orElseGet(() -> {
            Cart c = new Cart();
            c.setGuestToken(guestToken);
            return cartRepository.save(c);
        });
    }

    private User resolveUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        return (principal instanceof User) ? (User) principal : null;
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(i -> {
            BigDecimal price = i.getProduct().getPrice();
            BigDecimal line = price.multiply(BigDecimal.valueOf(i.getQuantity()));
            return CartItemResponse.builder()
                    .productId(i.getProduct().getId())
                    .productName(i.getProduct().getName())
                    .imageUrl(i.getProduct().getImageUrl())
                    .unitPrice(price)
                    .quantity(i.getQuantity())
                    .lineTotal(line)
                    .build();
        }).toList();
        BigDecimal total = items.stream().map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return CartResponse.builder().items(items).total(total).build();
    }
}
