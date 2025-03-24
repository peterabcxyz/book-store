package com.interswitch.bookstore.domains;

import com.interswitch.bookstore.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "tbl_books",
        indexes = {
                @Index(name = "idx_book_title", columnList = "title"),
                @Index(name = "idx_book_author", columnList = "author"),
                @Index(name = "idx_book_publication_year", columnList = "publicationYear"),
                @Index(name = "idx_book_genre", columnList = "genre")
        }
)
@Entity
public class Book extends Domain{

    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String isbn;

    private String author;

    private int publicationYear;

    private BigDecimal price;

    private int quantityInStock;

    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
