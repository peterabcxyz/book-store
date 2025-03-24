package com.interswitch.bookstore.controllers;

import com.interswitch.bookstore.requests.CartAddDTO;
import com.interswitch.bookstore.responses.ApiResponse;
import com.interswitch.bookstore.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/cart")
@RequiredArgsConstructor
@RestController
public class CartController {
    private final CartService cartService;

    /**
     * Adds a book to the user's shopping cart.
     *
     * @param request The CartAddDTO containing book and quantity information
     * @return ResponseEntity containing ApiResponse with the updated cart
     * @throws IllegalArgumentException if the request contains invalid data
     */
    @PostMapping("/add")
    @Operation(summary = "Add book to cart")
    public ResponseEntity<ApiResponse<?>> addToCart(@Valid @RequestBody CartAddDTO request) {
        var cart = cartService.addToCart(request);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Book added to cart successfully.", cart);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Retrieves the contents of a user's shopping cart.
     *
     * @param userId The ID of the user whose cart is to be viewed
     * @return ResponseEntity containing ApiResponse with the user's cart contents
     * @throws IllegalArgumentException if the userId is invalid or cart not found
     */
    @GetMapping("/{userId}")
    @Operation(summary = "View cart contents")
    public ResponseEntity<ApiResponse<?>> viewCart(@PathVariable Long userId) {
        var cart = cartService.getCart(userId);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "View cart successfully.", cart);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Clears all items from a user's shopping cart.
     *
     * @param userId The ID of the user whose cart should be cleared
     * @return ResponseEntity with NO_CONTENT status
     * @throws IllegalArgumentException if the userId is invalid
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "Clear user cart")
    public ResponseEntity<ApiResponse<?>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
