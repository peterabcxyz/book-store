package com.interswitch.bookstore.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "tbl_purchase_items")
@Entity
public class PurchaseItem extends Domain {

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private int quantity;

    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
