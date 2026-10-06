package com.example.purchase_invoices.dto;

import java.time.LocalDate;
import java.util.List;

public record InvoiceResponse(
        String accessToken,
        LocalDate invoiceEntryDate,
        List<ProductPurchaseResponse> products
){}
