package com.example.purchase_invoices;

public class Product {

    private String name;
    private String code;

    private String measure;

    private Double unitPrice;

    Product(String name, String code, String measure, Double unitPrice) {
        this.name = name;
        this.code = code;
        this.measure = measure;
        this.unitPrice = unitPrice;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }
}
