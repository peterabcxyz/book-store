package com.interswitch.bookstore.services;

import com.interswitch.bookstore.domains.Book;
import com.interswitch.bookstore.domains.Cart;
import com.interswitch.bookstore.exceptions.BadRequestException;
import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.mappers.CartMapper;
import com.interswitch.bookstore.repositories.BookRepository;
import com.interswitch.bookstore.repositories.CartRepository;
import com.interswitch.bookstore.requests.CartAddDTO;
import com.interswitch.bookstore.responses.CartDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private Book book;
    private CartDTO cartDTO;
    private CartAddDTO cartAddDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setQuantityInStock(10);

        cart = new Cart(1L);
        cart.setItems(new ArrayList<>());

        cartDTO = new CartDTO();
        cartDTO.setUserId(1L);

        cartAddDTO = new CartAddDTO();
        cartAddDTO.setUserId(1L);
        cartAddDTO.setBookId(1L);
        cartAddDTO.setQuantity(2);
    }

    @Test
    void addToCart_newCart_success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        CartDTO result = cartService.addToCart(cartAddDTO);

        assertNotNull(result);
        assertEquals(cartDTO, result);
        verify(cartRepository).findByUserId(1L);
        verify(bookRepository).findById(1L);
        verify(cartRepository).save(any(Cart.class));
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void addToCart_existingCart_success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        CartDTO result = cartService.addToCart(cartAddDTO);

        assertNotNull(result);
        assertEquals(cartDTO, result);
        verify(cartRepository).findByUserId(1L);
        verify(bookRepository).findById(1L);
        verify(cartRepository).save(cart);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void addToCart_bookNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.addToCart(cartAddDTO));
        verify(cartRepository).findByUserId(1L);
        verify(bookRepository).findById(1L);
        verifyNoMoreInteractions(cartRepository, cartMapper);
    }

    @Test
    void addToCart_insufficientStock() {
        book.setQuantityInStock(1);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BadRequestException.class, () -> cartService.addToCart(cartAddDTO));
        verify(cartRepository).findByUserId(1L);
        verify(bookRepository).findById(1L);
        verifyNoMoreInteractions(cartRepository, cartMapper);
    }

    @Test
    void getCart_success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toDTO(cart)).thenReturn(cartDTO);

        CartDTO result = cartService.getCart(1L);

        assertNotNull(result);
        assertEquals(cartDTO, result);
        verify(cartRepository).findByUserId(1L);
        verify(cartMapper).toDTO(cart);
    }

    @Test
    void getCart_notFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.getCart(1L));
        verify(cartRepository).findByUserId(1L);
        verifyNoInteractions(cartMapper);
    }

    @Test
    void clearCart_success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.clearCart(1L);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).findByUserId(1L);
        verify(cartRepository).save(cart);
    }

    @Test
    void clearCart_notFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.clearCart(1L));
        verify(cartRepository).findByUserId(1L);
        verifyNoMoreInteractions(cartRepository);
    }
}