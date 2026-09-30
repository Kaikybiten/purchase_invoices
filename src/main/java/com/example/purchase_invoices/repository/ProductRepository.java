package com.example.purchase_invoices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.purchase_invoices.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Conexão com o banco de dados
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT code, unit_price WHERE code IN :codes", nativeQuery=true)
    List<Object[]> getAllByCodes(@Param("codes") List<String> codes);
}
