package com.ecommerce_nexus.backend.cart;

import com.ecommerce_nexus.backend.cart.dto.CartItemRequest;
import com.ecommerce_nexus.backend.cart.dto.CartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getMyCart(
            @RequestParam(value = "guestToken", required = false) String guestToken) {
        return ResponseEntity.ok(cartService.getMyCart(guestToken));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @Valid @RequestBody CartItemRequest request,
            @RequestParam(value = "guestToken", required = false) String guestToken) {
        return ResponseEntity.ok(cartService.addItem(request, guestToken));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "quantity") int quantity,
            @RequestParam(value = "guestToken", required = false) String guestToken) {
        return ResponseEntity.ok(cartService.updateItem(productId, quantity, guestToken));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "guestToken", required = false) String guestToken) {
        return ResponseEntity.ok(cartService.removeItem(productId, guestToken));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(
            @RequestParam(value = "guestToken", required = false) String guestToken) {
        cartService.clearCart(guestToken);
        return ResponseEntity.noContent().build();
    }
}
