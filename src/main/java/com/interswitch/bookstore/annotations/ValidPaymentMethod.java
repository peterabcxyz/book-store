package com.interswitch.bookstore.annotations;

import com.interswitch.bookstore.validators.PaymentMethodValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PaymentMethodValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaymentMethod {

    String message() default "Invalid payment method. Allowed payment method are WEB, USSD and TRANSFER only.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}