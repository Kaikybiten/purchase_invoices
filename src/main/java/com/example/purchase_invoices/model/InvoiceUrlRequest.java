package com.example.purchase_invoices.model;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class InvoiceUrlRequest {

    @NotBlank(message = "{invoiceUrlRequest.Url.notBlank}")
    @URL(message = "{invoiceUrlRequest.Url.invalid}")
    private String url;

    public String getUrl() { return url; }
}
