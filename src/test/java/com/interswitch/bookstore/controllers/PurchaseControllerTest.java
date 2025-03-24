package com.interswitch.bookstore.controllers;

import com.interswitch.bookstore.requests.PurchaseCheckoutDTO;
import com.interswitch.bookstore.responses.ApiResponse;
import com.interswitch.bookstore.responses.PurchaseDTO;
import com.interswitch.bookstore.services.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController controller;

    private PurchaseDTO purchaseDTO;
    private PurchaseCheckoutDTO checkoutDTO;

    @BeforeEach
    void setUp() {
        purchaseDTO = new PurchaseDTO();
        purchaseDTO.setUserId(1L);

        checkoutDTO = new PurchaseCheckoutDTO();
    }

    @Test
    void getPurchaseHistory_success() {
        Page<PurchaseDTO> purchasePage = new PageImpl<>(List.of(purchaseDTO));
        when(purchaseService.getPurchaseHistory(eq(1L), any())).thenReturn(purchasePage);

        ResponseEntity<ApiResponse<?>> response = controller.getPurchaseHistory(1L, 0, 10, "createdAt", "desc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(purchasePage, response.getBody().data());
        verify(purchaseService).getPurchaseHistory(eq(1L), any());
    }

    @Test
    void checkout_success() {
        when(purchaseService.checkout(checkoutDTO)).thenReturn(purchaseDTO);

        ResponseEntity<ApiResponse<?>> response = controller.checkout(checkoutDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(purchaseDTO, response.getBody().data());
        verify(purchaseService).checkout(checkoutDTO);
    }
}