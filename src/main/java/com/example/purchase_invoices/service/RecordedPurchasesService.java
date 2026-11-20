package com.example.purchase_invoices.service;

import com.example.purchase_invoices.dto.InvoiceResponse;
import com.example.purchase_invoices.dto.ProductPurchaseResponse;
import com.example.purchase_invoices.model.Invoice;
import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.repository.RecordedPurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordedPurchasesService {

    @Autowired
    private RecordedPurchasesRepository recordedPurchasesRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceService invoiceService;


    private void saveInvoice(List<RecordedPurchases> recordedPurchases) {

        // Salvando nota
        recordedPurchases.stream()
                .findFirst() // Extrai primeira compra e unica
                .map(RecordedPurchases::getInvoice) // Extrai nota
                .ifPresent(invoiceService::save); // Salva nota no banco
    }

    public List<RecordedPurchases> saveValidProduct(
            List<RecordedPurchases> recordedPurchases
    ) {

        saveInvoice(recordedPurchases);

        List<Product> products = productService.processProduct(recordedPurchases);

        productService.saveAll(products);

        recordedPurchasesRepository.saveAll(recordedPurchases);

        return recordedPurchases;
    }

    public InvoiceResponse toResponse(
            List<RecordedPurchases> purchases
    ) {
        Invoice invoice = purchases.get(0).getInvoice();

        invoiceService.save(invoice);

        List<ProductPurchaseResponse> products = purchases.stream()
                .map(purchase -> {
                    Product product = purchase.getProduct();

                    return new ProductPurchaseResponse(
                            product.getCode(),
                            product.getName(),
                            product.getMeasure(),
                            product.getUnitPrice(),
                            purchase.getTotalPrice(),
                            purchase.getQuantity()
                    );
                })
                .toList();

        return new InvoiceResponse(
                invoice.getAccessToken(),
                invoice.getInvoiceEntryDate(),
                products
        );
    }
}
