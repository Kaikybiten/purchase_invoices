package com.example.purchase_invoices.controller;

import com.example.purchase_invoices.scrapper.ReaderHTML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.response.JsonResponse;
import com.example.purchase_invoices.service.ProductService;

import com.example.purchase_invoices.model.InvoiceUrlRequest;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<JsonResponse<Product>> getProduct() {

        List<Product> productList = productService.findAll();

        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(JsonResponse.error("Nenhum produto foi encontrado."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.success(productList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.success(product));
    }

    @PostMapping("/invoice")
    public ResponseEntity<JsonResponse<Product>> postProduct(
            @RequestBody InvoiceUrlRequest invoiceUrlRequest
    ) {
        List<Product> productList = ReaderHTML.getProducts(invoiceUrlRequest.getUrl());

        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    JsonResponse.error("Nenhum produto localizado na nota informada.")
            );
        }

        List<Product> productsSave = productService.saveValids(productList);

        return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.success(productsSave));
    }
}
