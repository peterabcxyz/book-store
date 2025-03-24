package com.interswitch.bookstore.validators;

import com.interswitch.bookstore.annotations.ValidGenre;
import com.interswitch.bookstore.enums.Genre;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class GenreValidator implements ConstraintValidator<ValidGenre, Genre> {

    private static final List<Genre> ALLOWED_GENRES = Arrays.asList(
            Genre.FICTION, Genre.THRILLER, Genre.MYSTERY, Genre.POETRY, Genre.HORROR, Genre.SATIRE
    );

    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation the annotation instance
     */
    @Override
    public void initialize(ValidGenre constraintAnnotation) {}

    /**
     * Validates if the given genre is in the allowed subset.
     *
     * @param genre The genre to validate
     * @param context The validation context
     * @return true if the genre is valid, false otherwise
     */
    @Override
    public boolean isValid(Genre genre, ConstraintValidatorContext context) {
        return ALLOWED_GENRES.contains(genre);
    }
}
