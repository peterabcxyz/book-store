package com.interswitch.bookstore.services;

import com.interswitch.bookstore.domains.Purchase;

public interface PaymentService {

    /**
     * Processes a payment for a given purchase.
     *
     * @param purchase The purchase object containing payment details to be processed
     */
    void processPayment(Purchase purchase);
}