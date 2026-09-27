package com.example.purchase_invoices.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Entity
@Table(name= "product")
@JsonPropertyOrder({"id", "name", "unitPrice", "measure", "code"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "code")
    @NotBlank(message = "{product.code.notBlank}")
    @Max(value = 30, message = "{product.code.max}")
    private String code;

    @Column(name = "name")
    @NotBlank(message = "{product.name.notBlank}")
    @Max(value = 30, message = "{product.name.max}")
    private String name;

    @Column(name = "measure")
    @Max(value = 10,message = "{product.measure.max}")
    private String measure;

    @Column(name = "unit_price")
    @NotNull(message = "{product.unitPrice.notNull}")
    @Positive(message = "{product.unitPrice.positive}")
    private Double unitPrice;

    public Product() {
    }

    public Product(String name, String code, String measure, Double unitPrice) {
        this.name = name;
        this.code = code;
        this.measure = measure;
        this.unitPrice = unitPrice;
    }

    public long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getMeasure() { return measure; }
    public Double getUnitPrice() { return unitPrice; }
}
