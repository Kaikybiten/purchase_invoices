package com.example.purchase_invoices.service;

import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.example.purchase_invoices.model.Product;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService (ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    private List<Product> addNewProduct(
            List<Product> existingProducts,
            List<RecordedPurchases> recordedPurchases
    ) {

        List<Product> newProducts = new ArrayList<>(); // Receberá produtos validos

        for (RecordedPurchases purchase : recordedPurchases) {

            Product product = purchase.getProduct();

            // Verificação se produto já está no banco
            Product existingProduct = existingProducts.stream()
                    .filter(existing -> {

                                String code = existing.getCode();
                                BigDecimal unitPrice = existing.getUnitPrice();

                                return code.equals(product.getCode())
                                    && unitPrice.compareTo(product.getUnitPrice()) == 0;
                            }

                            // Retorne o primeiro localizado, caso contrario retorne null
                    ).findFirst().orElse(null);

            // Caso haja registro do produto no banco de dados, apenas irá referenciar em 'recorded_purchases'
            if (existingProduct != null) {
                purchase.setProduct(existingProduct);
                continue;
            }

            // Valida como novo produto e registra novo produto no banco de dados
            product.setValid(true);
            newProducts.add(product);
        }

        return newProducts;
    }


    public List<Product> processProduct (List<RecordedPurchases> recordedPurchases) {

        // Extraindo todos os produtos
        List<Product> products = recordedPurchases.stream()
                .map(RecordedPurchases::getProduct)
                .toList();

        // Extraindo todos os codigos dos produtos - Para busca no banco
        List<String> codes = products.stream()
                .map(Product::getCode)
                .toList();

        // Buscando produtos já registrados
        List<Product> existingProducts = productRepository.findByCodeIn(codes);

        return addNewProduct(
                existingProducts,
                recordedPurchases
        );
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Não foi possivel localizar nenhum produto com o id %d.", id))
                );
    }

    public void saveAll(List<Product> products) {

        productRepository.saveAll(products);
    }
}
