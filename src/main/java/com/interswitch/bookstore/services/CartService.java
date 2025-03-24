package com.interswitch.bookstore.services;

import com.interswitch.bookstore.domains.Cart;
import com.interswitch.bookstore.domains.CartItem;
import com.interswitch.bookstore.exceptions.BadRequestException;
import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.mappers.CartMapper;
import com.interswitch.bookstore.repositories.BookRepository;
import com.interswitch.bookstore.repositories.CartRepository;
import com.interswitch.bookstore.requests.CartAddDTO;
import com.interswitch.bookstore.responses.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartMapper cartMapper;

    /**
     * Adds a book to a user's shopping cart with specified quantity.
     *
     * @param request The CartAddDTO containing user ID, book ID, and quantity
     * @return CartDTO representing the updated cart
     * @throws NotFoundException if the book is not found
     * @throws BadRequestException if there's insufficient stock for the requested quantity
     */
    public CartDTO addToCart(CartAddDTO request) {
        var cart = cartRepository.findByUserId(request.getUserId())
                .orElse(new Cart(request.getUserId()));

        var book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found."));

        if (book.getQuantityInStock() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock.");
        }

        var item = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(request.getBookId()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new CartItem(cart, book, 0);
            cart.getItems().add(item);
        }

        item.setQuantity(item.getQuantity() + request.getQuantity());

        var savedCart = cartRepository.save(cart);
        return cartMapper.toDTO(savedCart);
    }

    /**
     * Retrieves a user's shopping cart contents.
     *
     * @param userId The ID of the user whose cart is to be retrieved
     * @return CartDTO containing the cart details
     * @throws NotFoundException if no cart exists for the given user ID
     */
    public CartDTO getCart(Long userId) {
        var cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user: " + userId));
        return cartMapper.toDTO(cart);
    }

    /**
     * Clears all items from a user's shopping cart.
     *
     * @param userId The ID of the user whose cart should be cleared
     * @throws NotFoundException if no cart exists for the given user ID
     */
    public void clearCart(Long userId) {
        var cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found."));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}