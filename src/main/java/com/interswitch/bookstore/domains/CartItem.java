package com.interswitch.bookstore.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "tbl_cart_items")
@Entity
public class CartItem extends Domain {
    private Long id;

    @ManyToOne
    private Book book;

    private int quantity;

    @ManyToOne
    private Cart cart;

    public CartItem(Cart cart, Book book, int quantity) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
    }

    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
