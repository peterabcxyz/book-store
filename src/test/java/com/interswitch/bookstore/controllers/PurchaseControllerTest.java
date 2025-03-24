package com.interswitch.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.bookstore.enums.Genre;
import com.interswitch.bookstore.enums.PaymentMethod;
import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.requests.PurchaseCheckoutDTO;
import com.interswitch.bookstore.responses.BookDTO;
import com.interswitch.bookstore.responses.PurchaseDTO;
import com.interswitch.bookstore.responses.PurchaseItemDTO;
import com.interswitch.bookstore.services.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseDTO purchaseDTO;

    @BeforeEach
    void setUp() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setGenre(Genre.FICTION);

        purchaseDTO = new PurchaseDTO();
        purchaseDTO.setId(1L);
        purchaseDTO.setUserId(1L);
        purchaseDTO.setPaymentMethod(PaymentMethod.WEB);
        PurchaseItemDTO purchaseItemDTO = new PurchaseItemDTO();
        purchaseItemDTO.setBook(bookDTO);
        purchaseItemDTO.setQuantity(2);
        purchaseDTO.setItems(Collections.singletonList(purchaseItemDTO));
    }

    @Test
    void getPurchaseHistory_Success() throws Exception {
        Page<PurchaseDTO> purchasePage = new PageImpl<>(Collections.singletonList(purchaseDTO));
        when(purchaseService.getPurchaseHistory(eq(1L), any())).thenReturn(purchasePage);

        mockMvc.perform(get("/api/purchases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].userId").value(1));
    }

    @Test
    void getPurchaseHistory_NotFound() throws Exception {
        when(purchaseService.getPurchaseHistory(eq(1L), any()))
                .thenThrow(new NotFoundException("Purchase history not found"));

        mockMvc.perform(get("/api/purchases/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Purchase history not found"));
    }

    @Test
    void checkout_Success() throws Exception {
        PurchaseCheckoutDTO request = new PurchaseCheckoutDTO();
        request.setUserId(1L);
        request.setPaymentMethod(PaymentMethod.WEB);

        when(purchaseService.checkout(any(PurchaseCheckoutDTO.class))).thenReturn(purchaseDTO);

        mockMvc.perform(post("/api/purchases/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    void checkout_NotFound() throws Exception {
        PurchaseCheckoutDTO request = new PurchaseCheckoutDTO();
        request.setUserId(1L);
        request.setPaymentMethod(PaymentMethod.WEB);

        when(purchaseService.checkout(any())).thenThrow(new NotFoundException("Cart not found"));

        mockMvc.perform(post("/api/purchases/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Cart not found"));
    }

    @Test
    void checkout_BadRequest_InvalidPaymentMethod() throws Exception {
        PurchaseCheckoutDTO request = new PurchaseCheckoutDTO();
        request.setUserId(1L);
        request.setPaymentMethod(null); // Invalid payment method

        mockMvc.perform(post("/api/purchases/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }
}