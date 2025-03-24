package com.interswitch.bookstore.repositories;

import com.interswitch.bookstore.domains.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Searches for books matching the given search term across multiple fields.
     * The search is case-insensitive and matches against title, author, publication year, and genre.
     *
     * @param searchTerm The term to search for (can be null for all books)
     * @param pageable Pagination information including page size and number
     * @return Page<Book> containing the matching books with pagination applied
     */
    @Query("SELECT b FROM Book b WHERE " +
            ":searchTerm IS NULL OR " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "CAST(b.publicationYear AS string) LIKE CONCAT('%', :searchTerm, '%') OR " +
            "LOWER(b.genre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Book> searchBooks(String searchTerm, Pageable pageable);
}