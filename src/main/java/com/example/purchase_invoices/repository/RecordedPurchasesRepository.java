package com.example.purchase_invoices.repository;

import com.example.purchase_invoices.model.RecordedPurchases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordedPurchasesRepository extends JpaRepository<RecordedPurchases, Long> {
}
