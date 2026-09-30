package com.example.purchase_invoices.service;

import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.repository.RecordedPurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordedPurchasesService {

    @Autowired
    private RecordedPurchasesRepository recordedPurchasesRepository;

    @Autowired
    private ProductService productService;

    public List<RecordedPurchases> saveValidProduct(
            List<RecordedPurchases> recordedPurchases
    ) {

        List<Product> products = recordedPurchases.stream()
                .map(RecordedPurchases::getProduct)
                .toList();

        // Obtendo codigos para busca em banco
        List<String> codes = products.stream()
                .map(Product::getCode)
                .toList();

        List<Product> existingProducts = productService.findByCodeIn(codes);

        List<Product> newProducts = new ArrayList<>();
        for (RecordedPurchases purchase : recordedPurchases) {

            Product product = purchase.getProduct();

            // Verificação se produto já esta no banco
            Product existingProduct = existingProducts.stream()
                    .filter(existing -> {

                        String code = existing.getCode();
                        BigDecimal unitPrice = product.getUnitPrice();

                        return code.equals(product.getCode())
                                && unitPrice.compareTo(product.getUnitPrice()) == 0;
                            }

                    ).findFirst().orElse(null);

            // Caso haja registro do produto no banco de dados, apenas irá referenciar em 'recorded_purchases'
            if (existingProduct != null) {
                purchase.setProduct(existingProduct);
                continue;
            }

            // Valida como novo produto e registra novo produto no banco da dados
            product.setValid(true);
            newProducts.add(product);
        }

        productService.saveAll(newProducts);

        recordedPurchasesRepository.saveAll(recordedPurchases);

        return recordedPurchases;
    }

    public List<RecordedPurchases> saveAll(List<RecordedPurchases> recordedPurchases) {

        recordedPurchasesRepository.saveAll(recordedPurchases);

        return recordedPurchases;
    }
}
