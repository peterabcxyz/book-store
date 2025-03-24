package com.interswitch.bookstore.annotations;

import com.interswitch.bookstore.validators.GenreValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = GenreValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGenre {

    String message() default "Invalid genre. Allowed genres are FICTION, THRILLER, MYSTERY, POETRY, HORROR and SATIRE only.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}