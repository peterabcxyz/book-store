package com.interswitch.bookstore.responses;

import com.interswitch.bookstore.enums.Genre;
import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private Genre genre;
    private String isbn;
    private String author;
    private int publicationYear;
    private String price;
    private int quantityInStock;
    private String createdAt;
    private String updatedAt;
}
