package com.interswitch.bookstore.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tbl_carts")
@Entity
@Data
public class Cart extends Domain {

    private Long userId;

    public Cart(Long userId){
        this.userId = userId;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();

    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
