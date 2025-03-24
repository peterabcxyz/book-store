package com.interswitch.bookstore.responses;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private String createdAt;
    private String updatedAt;
}
