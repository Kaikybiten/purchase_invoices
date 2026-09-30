package com.example.purchase_invoices.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name= "product")
@JsonPropertyOrder({ "id", "valid", "name", "unitPrice", "measure", "code" })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    @NotBlank(message = "{product.code.notBlank}")
    @Size(max = 30, message = "{product.code.max}")
    private String code;

    @Column(name = "name")
    @NotBlank(message = "{product.name.notBlank}")
    @Size(max = 30, message = "{product.name.max}")
    private String name;

    @Column(name = "measure")
    @Size(max = 10,message = "{product.measure.max}")
    private String measure;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "{product.unitPrice.notNull}")
    @Positive(message = "{product.unitPrice.positive}")
    private BigDecimal unitPrice;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    // Exclui o campo do mapeamento JPA
    @Transient
    private boolean valid = false;

    public Product() {
    }

    public Product(String name, String code, String measure, BigDecimal unitPrice) {
        this.name = name;
        this.code = code;
        this.measure = measure;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getMeasure() { return measure; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

}
