package com.example.purchase_invoices.service;

import com.example.purchase_invoices.repository.ProductRepository;
import jakarta.el.MethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


    public List<Object[]> findByCodes(List<String> codes) {
        return productRepository.getAllByCodes(codes);
    }

    public List<Product> save (Product product) {

        productRepository.save(product);

        return new ArrayList<>(List.of(product));
    }

    public  List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }
}
