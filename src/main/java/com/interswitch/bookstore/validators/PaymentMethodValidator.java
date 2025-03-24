package com.interswitch.bookstore.validators;

import com.interswitch.bookstore.annotations.ValidPaymentMethod;
import com.interswitch.bookstore.enums.PaymentMethod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class PaymentMethodValidator implements ConstraintValidator<ValidPaymentMethod, PaymentMethod> {

    private static final List<PaymentMethod> ALLOWED_PAYMENT_METHODS = Arrays.asList(
            PaymentMethod.WEB, PaymentMethod.USSD, PaymentMethod.TRANSFER
    );

    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation the annotation instance
     */
    @Override
    public void initialize(ValidPaymentMethod constraintAnnotation) {}

    /**
     * Validates if the given payment method is in the allowed subset.
     *
     * @param paymentMethod The genre to validate
     * @param context The validation context
     * @return true if the payment method is valid, false otherwise
     */
    @Override
    public boolean isValid(PaymentMethod paymentMethod, ConstraintValidatorContext context) {
        return ALLOWED_PAYMENT_METHODS.contains(paymentMethod);
    }
}
