package com.interswitch.bookstore.services;

import com.interswitch.bookstore.domains.Purchase;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    /**
     * Processes a payment for a given purchase by delegating to the simulation method.
     *
     * @param purchase The purchase object containing payment details to be processed
     */
    @Override
    public void processPayment(Purchase purchase) {
        simulatePayment(purchase);
    }

    /**
     * Simulates payment processing for a purchase.
     *
     * @param purchase The purchase to process payment for
     */
    public void simulatePayment(Purchase purchase) {
        // Simulate payment processing based on payment method
        switch (purchase.getPaymentMethod()) {
            case WEB:
                //  web payment
                break;
            case USSD:
                //  USSD payment
                break;
            case TRANSFER:
                // bank transfer
                break;
        }
    }
}

