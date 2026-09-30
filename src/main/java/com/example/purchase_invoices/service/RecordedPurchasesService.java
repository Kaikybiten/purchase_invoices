package com.example.purchase_invoices.service;

import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.repository.RecordedPurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordedPurchasesService {

    @Autowired
    private RecordedPurchasesRepository recordedPurchasesRepository;

    @Autowired
    private ProductService productService;

    public List<RecordedPurchases> saveValidProduct (List<RecordedPurchases> recordedPurchases){

        List<Product> products = recordedPurchases.stream().map(RecordedPurchases::getProduct).toList();

        List<String> codes =  products.stream().map(Product::getCode).toList();

        List<Object[]> codePrice = productService.findByCodes(codes);

        List<Product> validProducts = products.stream().filter(product ->
                        codePrice.stream().noneMatch(existing ->
                                existing[0].equals(product.getCode()) && existing[1].equals(product.getUnitPrice())
                        )
        ).toList();

        validProducts.forEach(product -> {product.setValid(true);});

        productService.saveAll(validProducts);
        recordedPurchasesRepository.saveAll(recordedPurchases);

        return recordedPurchases;
    }

    public List<RecordedPurchases> saveAll(List<RecordedPurchases> recordedPurchases) {

        recordedPurchasesRepository.saveAll(recordedPurchases);

        return recordedPurchases;
    }
}
