package com.example.purchase_invoices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.purchase_invoices.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Conexão com o banco de dados
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find by 'code' in (codes), Identifica 'code' em @Entity e utiliza a referencia para busca-lo no banco
    List<Product> findByCodeIn(List<String> codes);
}
