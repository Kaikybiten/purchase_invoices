package com.example.purchase_invoices;

import com.example.purchase_invoices.dto.InvoiceResponse;
import com.example.purchase_invoices.dto.ProductPurchaseResponse;
import com.example.purchase_invoices.model.Invoice;
import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.repository.RecordedPurchasesRepository;
import com.example.purchase_invoices.service.InvoiceService;
import com.example.purchase_invoices.service.ProductService;
import com.example.purchase_invoices.service.RecordedPurchasesService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class) // Habilita funcionalidades Mock
public class RecordedPurchasesServiceTests {

    @Mock // Objeto simulado
    private RecordedPurchasesRepository recordedPurchasesRepository;

    @Mock
    private ProductService productService;

    @Mock
    private InvoiceService invoiceService;

    private RecordedPurchasesService recordedPurchasesService;

    @BeforeEach
    void setUp() {
        recordedPurchasesService = new RecordedPurchasesService(
                recordedPurchasesRepository,
                productService,
                invoiceService
        );
    }

    @Test
    @DisplayName("Deve verificar os produtos e registrar aqueles que ainda não estão registrados no banco.")
    void mustRegisterProducts() {

        // Arrange: preparação dos dados
        LocalDate now = LocalDate.now();

        Invoice invoice = new Invoice("123456789", now);

        Product product = createProductBase();

        List<RecordedPurchases> purchasesList = List.of(createRecordedPurchasesBase(
                now, invoice, product
        ));

        List<Product> productList = List.of(product);


        // Valida comportamento do método
        when(productService.processProduct(purchasesList)).thenReturn(productList);


        // Act: execução do método real
        List<RecordedPurchases> purchasesListValid =
                recordedPurchasesService.saveValidProduct(purchasesList);


        // Assert: verificações
        assertSame(purchasesList, purchasesListValid);

        verify(invoiceService).save(invoice);
        verify(productService).processProduct(purchasesList);
        verify(productService).saveAll(productList);
        verify(recordedPurchasesRepository).saveAll(purchasesList);
    }

    @Test
    @DisplayName("Deve gerar um objeto base servir como response da requisição.")
    void mustGenerateObjectResponse() {

        // Arrange: preparação dos dados
        LocalDate now = LocalDate.now();

        Invoice invoice = new Invoice("123456789", now);
        List<RecordedPurchases> purchasesList = List.of(createRecordedPurchasesBase(
                now, invoice, createProductBase()
        ));


        // Act: execução do método real
        InvoiceResponse invoiceResponse = recordedPurchasesService.toResponse(purchasesList);

        // Assert: verificações
        assertEquals("123456789", invoiceResponse.accessToken());
        assertEquals(now, invoiceResponse.invoiceEntryDate());
        assertEquals(1, invoiceResponse.products().size());

        ProductPurchaseResponse product = invoiceResponse.products().getFirst();
        assertEquals("Tomate", product.name());
        assertEquals("0132456789", product.code());
    }

    @Test
    @DisplayName("Deve gerar um objeto base servir como response da requisição, porém agora com multiplos produtos.")
    void mustGenerateObjectResponseMultiplesProducts() {

        // Arrange: preparação dos dados
        LocalDate now = LocalDate.now();

        Invoice invoice = new Invoice("123456789", now);
        List<RecordedPurchases> purchasesList = new ArrayList<>();

        purchasesList.add(createRecordedPurchasesBase(now,
                invoice,
                new Product("Pão", "01324321", "UN", new BigDecimal("3"))
        ));
        purchasesList.add(createRecordedPurchasesBase(now,
                invoice,
                new Product("Leite", "04566789", "UN", new BigDecimal("3"))
        ));
        purchasesList.add(createRecordedPurchasesBase(now,
                invoice,
                new Product("Açúcar", "03165499", "KG", new BigDecimal("3"))
        ));


        // Act: execução do método real
        InvoiceResponse invoiceResponse = recordedPurchasesService.toResponse(purchasesList);

        // Assert: verificações
        assertEquals("123456789", invoiceResponse.accessToken());
        assertEquals(now, invoiceResponse.invoiceEntryDate());
        assertEquals(3, invoiceResponse.products().size());

        assertEquals("Pão", invoiceResponse.products().get(0).name());
        assertEquals("Leite", invoiceResponse.products().get(1).name());
        assertEquals("Açúcar", invoiceResponse.products().get(2).name());

        assertEquals("01324321", invoiceResponse.products().get(0).code());
        assertEquals("04566789", invoiceResponse.products().get(1).code());
        assertEquals("03165499", invoiceResponse.products().get(2).code());
    }

    private Product createProductBase() {
        return new Product("Tomate", "0132456789", "UN", new BigDecimal("3"));
    }

    private RecordedPurchases createRecordedPurchasesBase(LocalDate now, Invoice invoice, Product product) {
        // Gerando como lista
        return new RecordedPurchases(
                invoice,
                product,
                new BigDecimal("3"),
                new BigDecimal("1"),
                now
        );
    }
}
