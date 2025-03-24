package com.interswitch.bookstore.responses;

import com.interswitch.bookstore.enums.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseDTO {
    private Long id;
    private Long userId;
    private String purchaseDate;
    private List<PurchaseItemDTO> items;
    private PaymentMethod paymentMethod;
    private String createdAt;
    private String updatedAt;
}
