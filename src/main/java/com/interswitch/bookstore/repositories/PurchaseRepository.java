package com.interswitch.bookstore.repositories;

import com.interswitch.bookstore.domains.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    /**
     * Retrieves a paginated list of purchase history for a specific user.
     *
     * @param userId   the ID of the user whose purchase history is to be retrieved
     * @param pageable pagination information including page number, page size, and sorting
     * @return a Page containing Purchase entities for the specified user
     */
    @Query("SELECT p FROM Purchase p WHERE p.userId = :userId")
    Page<Purchase> purchaseHistory(Long userId, Pageable pageable);
}
