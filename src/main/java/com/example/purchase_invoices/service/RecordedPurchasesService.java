package com.example.purchase_invoices.service;

import com.example.purchase_invoices.dto.InvoiceResponse;
import com.example.purchase_invoices.dto.ProductPurchaseResponse;
import com.example.purchase_invoices.model.Invoice;
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

    @Autowired
    private InvoiceService invoiceService;

    public List<RecordedPurchases> saveValidProduct(
            List<RecordedPurchases> recordedPurchases
    ) {

        // Salvando nota
        Invoice invoice = recordedPurchases.get(0).getInvoice();
        invoiceService.save(invoice);


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

            // Verificação se produto já está no banco
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

    public InvoiceResponse toResponse(
            List<RecordedPurchases> purchases
    ) {
        Invoice invoice = purchases.get(0).getInvoice();

        Invoice savedInvoice = invoiceService.save(invoice);

        System.out.println("INVOICE ID: " + savedInvoice.getId());

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

    public List<RecordedPurchases> saveAll(List<RecordedPurchases> recordedPurchases) {

        recordedPurchasesRepository.saveAll(recordedPurchases);

        return recordedPurchases;
    }
}
