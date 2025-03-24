package com.interswitch.bookstore.repositories;

import com.interswitch.bookstore.domains.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Finds all cart items associated with the specified cart ID.
     *
     * @param cartId The ID of the cart whose items are to be retrieved
     * @return List of CartItem objects associated with the given cart ID
     */
    List<CartItem> findByCartId(Long cartId);
}