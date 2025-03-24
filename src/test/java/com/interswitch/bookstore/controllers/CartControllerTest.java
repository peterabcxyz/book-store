package com.interswitch.bookstore.controllers;

import com.interswitch.bookstore.requests.CartAddDTO;
import com.interswitch.bookstore.responses.ApiResponse;
import com.interswitch.bookstore.responses.CartDTO;
import com.interswitch.bookstore.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController controller;

    private CartDTO cartDTO;
    private CartAddDTO cartAddDTO;

    @BeforeEach
    void setUp() {
        cartDTO = new CartDTO();
        cartDTO.setUserId(1L);

        cartAddDTO = new CartAddDTO();
    }

    @Test
    void addToCart_success() {
        when(cartService.addToCart(cartAddDTO)).thenReturn(cartDTO);

        ResponseEntity<ApiResponse<?>> response = controller.addToCart(cartAddDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cartDTO, response.getBody().data());
        verify(cartService).addToCart(cartAddDTO);
    }

    @Test
    void viewCart_success() {
        when(cartService.getCart(1L)).thenReturn(cartDTO);

        ResponseEntity<ApiResponse<?>> response = controller.viewCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cartDTO, response.getBody().data());
        verify(cartService).getCart(1L);
    }

    @Test
    void clearCart_success() {
        doNothing().when(cartService).clearCart(1L);

        ResponseEntity<ApiResponse<?>> response = controller.clearCart(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService).clearCart(1L);
    }
}