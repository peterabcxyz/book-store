package com.interswitch.bookstore.repositories;

import com.interswitch.bookstore.domains.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Finds a cart associated with the specified user ID.
     *
     * @param userId The ID of the user whose cart is to be retrieved
     * @return Optional containing the Cart if found, empty otherwise
     */
    Optional<Cart> findByUserId(Long userId);
}
