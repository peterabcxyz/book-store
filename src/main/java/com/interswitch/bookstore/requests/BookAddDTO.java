package com.interswitch.bookstore.requests;

import com.interswitch.bookstore.enums.Genre;
import com.interswitch.bookstore.annotations.ValidGenre;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BookAddDTO {
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only letters and numbers")
    private String title;

    @ValidGenre
    private Genre genre;

    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only numbers and dashes")
    private String isbn;

    private String author;

    @Min(1000)
    @Max(9999)
    private int publicationYear;

    @NotNull(message = "quantityInStock is required.")
    @Min(value = 1, message = "quantityInStock must be greater than zero.")
    private int quantityInStock;

    @NotNull(message = "price is required.")
    @Min(value = 1, message = "price must be greater than zero.")
    private double price;
}

