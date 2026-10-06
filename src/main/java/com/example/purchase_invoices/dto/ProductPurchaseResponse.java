package com.example.purchase_invoices.dto;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
    String code,
    String name,
    String measure,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    BigDecimal quantity
) {
}
