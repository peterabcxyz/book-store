package com.interswitch.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.bookstore.enums.Genre;
import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.requests.CartAddDTO;
import com.interswitch.bookstore.responses.BookDTO;
import com.interswitch.bookstore.responses.CartDTO;
import com.interswitch.bookstore.responses.CartItemDTO;
import com.interswitch.bookstore.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartDTO cartDTO;

    @BeforeEach
    void setUp() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setGenre(Genre.FICTION);

        cartDTO = new CartDTO();
        cartDTO.setId(1L);
        cartDTO.setUserId(1L);
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setBook(bookDTO);
        cartItemDTO.setQuantity(2);
        cartDTO.setItems(Collections.singletonList(cartItemDTO));
    }

    @Test
    void addToCart_Success() throws Exception {
        CartAddDTO request = new CartAddDTO();
        request.setUserId(1L);
        request.setBookId(1L);
        request.setQuantity(2);

        when(cartService.addToCart(any(CartAddDTO.class))).thenReturn(cartDTO);

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    void addToCart_BadRequest_InvalidQuantity() throws Exception {
        CartAddDTO request = new CartAddDTO();
        request.setUserId(1L);
        request.setBookId(1L);
        request.setQuantity(-1); // Invalid quantity

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void viewCart_Success() throws Exception {
        when(cartService.getCart(1L)).thenReturn(cartDTO);

        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    void viewCart_NotFound() throws Exception {
        when(cartService.getCart(1L)).thenThrow(new NotFoundException("Cart not found"));

        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Cart not found"));
    }

    @Test
    void clearCart_Success() throws Exception {
        doNothing().when(cartService).clearCart(1L);

        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void clearCart_NotFound() throws Exception {
        doThrow(new NotFoundException("Cart not found")).when(cartService).clearCart(1L);

        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Cart not found"));
    }
}