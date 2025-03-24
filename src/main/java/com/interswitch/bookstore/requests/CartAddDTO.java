package com.interswitch.bookstore.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartAddDTO {
    @NotNull(message = "userId is required.")
    private Long userId;

    @NotNull(message = "bookId is required.")
    private Long bookId;

    @NotNull(message = "quantity is required.")
    private int quantity;
}
