package com.example.purchase_invoices.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "invoice_entry_date")
    private LocalDate invoiceEntryDate;

    protected Invoice(){
    }

    public Invoice(String accessToken, LocalDate invoiceEntryDate) {
        this.accessToken = accessToken;
        this.invoiceEntryDate = invoiceEntryDate;
    }

    public Long getId() { return id; }
    public String getAccessToken() {return accessToken;}
    public LocalDate getInvoiceEntryDate() {return invoiceEntryDate;}
}
