package com.example.purchase_invoices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.purchase_invoices.model.Product;

// Conexão com o banco de dados
public interface ProductRepository extends JpaRepository<Product, Long> {
}
