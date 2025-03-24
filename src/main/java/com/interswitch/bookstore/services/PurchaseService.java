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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CartRepository cartRepository;
    private final PurchaseMapper purchaseMapper;
    private final BookRepository bookRepository;
    private final PaymentService paymentService;
    private final CartService cartService;

    /**
     * Processes a checkout operation for a user's shopping cart.
     *
     * @param request the {@link PurchaseCheckoutDTO} containing the user ID and payment method
     *                for the checkout process
     * @return a {@link PurchaseDTO} representing the completed purchase
     * @throws NotFoundException if no cart is found for the specified user ID
     * @throws BadRequestException if the cart is empty or if there is insufficient stock
     *                             for any book in the cart
     */
    @Transactional
    public PurchaseDTO checkout(@RequestBody PurchaseCheckoutDTO request) {
        var cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Cart not found for user: " + request.getUserId()));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot checkout empty cart.");
        }

        // Update inventory
        for (var item : cart.getItems()) {
            var book = bookRepository.findById(item.getBook().getId())
                    .orElseThrow(() -> new IllegalStateException("Book not found."));

            if (book.getQuantityInStock() < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for book: " + book.getTitle());
            }
            book.setQuantityInStock(book.getQuantityInStock() - item.getQuantity());
            bookRepository.save(book);
        }

        // Create purchase record
        var purchase = new Purchase();
        purchase.setUserId(request.getUserId());
        purchase.setPaymentMethod(request.getPaymentMethod());
        purchase.setPurchaseDate(LocalDateTime.now());

        // Map cart items to purchase items
        var purchaseItems = cart.getItems().stream()
                .map(cartItem -> {
                    var purchaseItem = new PurchaseItem();
                    purchaseItem.setBook(cartItem.getBook());
                    purchaseItem.setQuantity(cartItem.getQuantity());
                    return purchaseItem;
                })
                .collect(Collectors.toList());

        purchase.setItems(purchaseItems);

        // Process payment
        paymentService.processPayment(purchase);

        // Save purchase and clear cart
        var savedPurchase = purchaseRepository.save(purchase);
        cartService.clearCart(request.getUserId());

        return purchaseMapper.toDTO(savedPurchase);
    }

    /**
     * Retrieves the purchase history for a specific user with pagination support.
     *
     * @param userId The ID of the user whose purchase history is being retrieved
     * @param pageable Pagination information including page number, size, and sorting
     * @return Page<PurchaseDTO> containing a page of purchase history records
     */
    public Page<PurchaseDTO> getPurchaseHistory(Long userId, Pageable pageable) {
        var purchases = purchaseRepository.purchaseHistory(userId, pageable);
        return purchases.map(purchaseMapper::toDTO);
    }
}