package com.example.purchase_invoices.exception;

public class InvoiceReadException extends RuntimeException {

    public InvoiceReadException(String message, Throwable cause) {

        super(message, cause);
    }
}
