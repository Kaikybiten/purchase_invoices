package com.example.purchase_invoices.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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

    public RecordedPurchases(Product product, BigDecimal totalPrice, BigDecimal quantity) {

        BigDecimal productUnitPrice = product.getUnitPrice();
        BigDecimal allegedUnitPrice = totalPrice.divide(quantity, 21, BigDecimal.ROUND_HALF_UP);

        if (!Objects.equals(allegedUnitPrice, productUnitPrice)) {
            System.out.println("O valor do produto não está condizente.");
            return;
        }

        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() { return id; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public BigDecimal getQuantity() { return quantity; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public Product getProduct() { return product; }
}