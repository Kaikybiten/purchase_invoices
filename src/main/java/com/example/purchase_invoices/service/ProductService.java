package com.example.purchase_invoices.service;

import com.example.purchase_invoices.repository.ProductRepository;
import jakarta.el.MethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.purchase_invoices.model.Product;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {

        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new MethodNotFoundException(
                        String.format("Não foi possivel localizar nenhum produto com o id %d.", id))
                );
    }
}
