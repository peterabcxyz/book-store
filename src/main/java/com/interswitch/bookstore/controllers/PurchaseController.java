package com.interswitch.bookstore.controllers;

import com.interswitch.bookstore.requests.PurchaseCheckoutDTO;
import com.interswitch.bookstore.responses.ApiResponse;
import com.interswitch.bookstore.services.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing book purchases.
 * Provides endpoints for retrieving a user's purchase history and processing checkout operations.
 * All responses are wrapped in an {@link ApiResponse} object for consistent API output.
 */
@Tag(name = "Purchase Service APIs", description = "API for book purchase management")
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@RestController
public class PurchaseController {
    private final PurchaseService purchaseService;

    /**
     * Retrieves the purchase history for a specified user, paginated and sorted based on request parameters.
     *
     * @param userId the ID of the user whose purchase history is to be retrieved
     * @param page the page number to retrieve (default is 0)
     * @param size the number of records per page (default is 10)
     * @param sortBy the field to sort by (default is "createdAt")
     * @param sortDirection the direction of sorting ("asc" or "desc", default is "desc")
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with the purchase history data
     * @throws IllegalArgumentException if the pagination or sorting parameters are invalid
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get purchase history")
    public ResponseEntity<ApiResponse<?>> getPurchaseHistory(@PathVariable Long userId,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                                             @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                             @RequestParam(value = "sort-direction", defaultValue = "desc") String sortDirection) {
        var direction =
                "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, JpaSort.unsafe(direction, sortBy));
        var purchaseHistory = purchaseService.getPurchaseHistory(userId, pageable);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Purchase history retrieved successfully.", purchaseHistory);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Processes a checkout request using the provided payment method and cart details.
     * The request is validated before processing, and the result is returned as a DTO.
     *
     * @param request the {@link PurchaseCheckoutDTO} containing the user ID and payment method
     *                for the checkout process
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with the checkout result
     * @throws jakarta.validation.ConstraintViolationException if the request data is invalid
     * @throws NullPointerException if the request is null
     */
    @PostMapping("/checkout")
    @Operation(summary = "Checkout with selected payment method")
    public ResponseEntity<ApiResponse<?>> checkout(@Valid @RequestBody PurchaseCheckoutDTO request) {
        var checkout = purchaseService.checkout(request);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Purchase checkout successful.", checkout);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}