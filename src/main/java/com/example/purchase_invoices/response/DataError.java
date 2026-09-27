package com.example.purchase_invoices.response;

public class DataError {

    private String field;
    private String error;

    public DataError(String field, String error) {
        this.field = field;
        this.error = error;
    }
    public String getField() { return field; }
    public String getError() { return error; }
}
