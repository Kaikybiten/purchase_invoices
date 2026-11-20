package com.example.purchase_invoices;

import com.example.purchase_invoices.model.Invoice;
import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.repository.RecordedPurchasesRepository;
import com.example.purchase_invoices.service.InvoiceService;
import com.example.purchase_invoices.service.ProductService;
import com.example.purchase_invoices.service.RecordedPurchasesService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;


// Habilita funcionalidades Mock
@ExtendWith(MockitoExtension.class)
public class RecordedPurchasesServiceTests {

    @Mock // Objeto simulado
    private RecordedPurchasesRepository recordedPurchasesRepository;

    @Mock
    private ProductService productService;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks // Instancia real, porém com o repositorio simulado em @Mock
    private RecordedPurchasesService recordedPurchasesService;


    @Test
    void deveRegistrarProdutos() {

        // Arrange: preparação dos dados

        List<RecordedPurchases> purchasesList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();

        Invoice invoice = new Invoice("123456789", LocalDate.now());

        Product product = new Product(
                "Tomate", "0132456789", "UN", new BigDecimal("3")
        );
        productList.add(product);

        RecordedPurchases recordedPurchases = new RecordedPurchases(
                invoice,
                product,
                new BigDecimal("3"),
                new BigDecimal("1"),
                invoice.getInvoiceEntryDate()
        );

        purchasesList.add(recordedPurchases);

        // Valida comportamento do método
        when(productService.processProduct(purchasesList))
                .thenReturn(productList);


        // Act: execução do método real
        List<RecordedPurchases> purchasesListValid = recordedPurchasesService.saveValidProduct(purchasesList);


        // Assert: verificações
        assertSame(purchasesList, purchasesListValid);

        verify(invoiceService).save(invoice);
        verify(productService).processProduct(purchasesList);
        verify(productService).saveAll(productList);
        verify(recordedPurchasesRepository).saveAll(purchasesList);
    }

}
