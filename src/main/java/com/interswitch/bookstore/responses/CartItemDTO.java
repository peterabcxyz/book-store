package com.interswitch.bookstore.responses;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private BookDTO book;
    private int quantity;
    private String createdAt;
    private String updatedAt;
}
