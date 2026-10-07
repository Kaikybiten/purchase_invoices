package com.example.purchase_invoices.repository;

import com.example.purchase_invoices.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    boolean existsByAccessToken(String accessToken);

}