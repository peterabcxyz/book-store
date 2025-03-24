package com.interswitch.bookstore.services;

import com.interswitch.bookstore.domains.*;
import com.interswitch.bookstore.exceptions.BadRequestException;
import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.mappers.PurchaseMapper;
import com.interswitch.bookstore.repositories.BookRepository;
import com.interswitch.bookstore.repositories.CartRepository;
import com.interswitch.bookstore.repositories.PurchaseRepository;
import com.interswitch.bookstore.requests.PurchaseCheckoutDTO;
import com.interswitch.bookstore.responses.PurchaseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private PurchaseService purchaseService;

    private Cart cart;
    private Book book;
    private CartItem cartItem;
    private Purchase purchase;
    private PurchaseDTO purchaseDTO;
    private PurchaseCheckoutDTO checkoutDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setQuantityInStock(10);

        cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        cart = new Cart(1L);
        cart.setItems(new ArrayList<>(List.of(cartItem)));

        purchase = new Purchase();
        purchaseDTO = new PurchaseDTO();
        purchaseDTO.setUserId(1L);

        checkoutDTO = new PurchaseCheckoutDTO();
        checkoutDTO.setUserId(1L);
    }

    @Test
    void checkout_success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.checkout(checkoutDTO);

        assertNotNull(result);
        assertEquals(purchaseDTO, result);
        verify(cartRepository).findByUserId(1L);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(book);
        verify(paymentService).processPayment(any(Purchase.class));
        verify(cartService).clearCart(1L);
    }

    @Test
    void checkout_cartNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> purchaseService.checkout(checkoutDTO));
        verify(cartRepository).findByUserId(1L);
        verifyNoMoreInteractions(bookRepository, paymentService, cartService);
    }

    @Test
    void checkout_emptyCart() {
        cart.setItems(new ArrayList<>());
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(BadRequestException.class, () -> purchaseService.checkout(checkoutDTO));
        verify(cartRepository).findByUserId(1L);
        verifyNoMoreInteractions(bookRepository, paymentService, cartService);
    }

    @Test
    void checkout_insufficientStock() {
        book.setQuantityInStock(1);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BadRequestException.class, () -> purchaseService.checkout(checkoutDTO));
        verify(cartRepository).findByUserId(1L);
        verify(bookRepository).findById(1L);
        verifyNoMoreInteractions(paymentService, cartService);
    }

    @Test
    void getPurchaseHistory_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Purchase> purchasePage = new PageImpl<>(List.of(purchase));
        when(purchaseRepository.purchaseHistory(1L, pageable)).thenReturn(purchasePage);
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        Page<PurchaseDTO> result = purchaseService.getPurchaseHistory(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(purchaseRepository).purchaseHistory(1L, pageable);
        verify(purchaseMapper).toDTO(purchase);
    }
}