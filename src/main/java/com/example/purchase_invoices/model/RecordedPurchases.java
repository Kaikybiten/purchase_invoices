package com.example.purchase_invoices.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "recorded_purchases")
@JsonPropertyOrder({ "id", "totalPrice", "quantity", "purchaseDate", "product" })
public class RecordedPurchases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    public RecordedPurchases(Product product, BigDecimal totalPrice, BigDecimal quantity, LocalDate purchaseDate) {

        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.product = product;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() { return id; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public BigDecimal getQuantity() { return quantity; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }
}