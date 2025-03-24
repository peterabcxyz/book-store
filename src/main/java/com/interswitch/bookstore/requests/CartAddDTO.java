package com.interswitch.bookstore.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartAddDTO {
    @NotNull(message = "userId is required.")
    private Long userId;

    @NotNull(message = "bookId is required.")
    private Long bookId;

    @NotNull(message = "quantity is required.")
    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;
}
