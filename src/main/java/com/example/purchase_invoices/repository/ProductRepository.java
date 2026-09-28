package com.example.purchase_invoices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.purchase_invoices.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Conexão com o banco de dados
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM product WHERE code = :code AND unit_price = :unit_price)", nativeQuery=true)
    boolean existsProduct(@Param("code") String code, @Param("unit_price")  double unit_price);
}
