package com.example.purchase_invoices;

import com.example.purchase_invoices.repository.RecordedPurchasesRepository;
import com.example.purchase_invoices.service.InvoiceService;
import com.example.purchase_invoices.service.ProductService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

// Habilita funcionalidades Mock
@ExtendWith(MockitoExtension.class)
public class RecordedPurchasesServiceTests {

    @Mock
    private RecordedPurchasesRepository recordedPurchasesRepository;

    @Mock
    private ProductService productService;

    @Mock
    private InvoiceService invoiceService;




}
