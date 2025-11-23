package com.ecommerce_nexus.backend.order.dto;

import com.ecommerce_nexus.backend.order.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @Valid
    @NotNull
    private Address shippingAddress;

    // Present only for guest checkout; ignored when user is authenticated
    private String guestToken;
}
