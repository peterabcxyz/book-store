package com.interswitch.bookstore.services;

import com.interswitch.bookstore.domains.Purchase;
import com.interswitch.bookstore.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Purchase purchase;

    @BeforeEach
    void setUp() {
        purchase = new Purchase();
    }

    @Test
    void processPayment_web() {
        purchase.setPaymentMethod(PaymentMethod.WEB);
        PaymentServiceImpl spyService = spy(paymentService);

        spyService.processPayment(purchase);

        verify(spyService).simulatePayment(purchase);
    }

    @Test
    void processPayment_ussd() {
        purchase.setPaymentMethod(PaymentMethod.USSD);
        PaymentServiceImpl spyService = spy(paymentService);

        spyService.processPayment(purchase);

        verify(spyService).simulatePayment(purchase);
    }

    @Test
    void processPayment_transfer() {
        purchase.setPaymentMethod(PaymentMethod.TRANSFER);
        PaymentServiceImpl spyService = spy(paymentService);

        spyService.processPayment(purchase);

        verify(spyService).simulatePayment(purchase);
    }
}