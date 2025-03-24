package com.interswitch.bookstore.requests;

import com.interswitch.bookstore.annotations.ValidPaymentMethod;
import com.interswitch.bookstore.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseCheckoutDTO {
    @NotNull(message = "userId is required.")
    private Long userId;

    @ValidPaymentMethod
    private PaymentMethod paymentMethod;
}
