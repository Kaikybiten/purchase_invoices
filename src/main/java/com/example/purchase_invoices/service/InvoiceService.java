package com.example.purchase_invoices.service;

import com.example.purchase_invoices.model.Invoice;
import com.example.purchase_invoices.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService (InvoiceRepository invoiceRepository) {

        this.invoiceRepository = invoiceRepository;
    }

    public void save(Invoice invoice) {

        invoiceRepository.save(invoice);
    }

    public boolean existsByAccessToken(String accessToken) {
        return invoiceRepository.existsByAccessToken(accessToken);
    }
}
