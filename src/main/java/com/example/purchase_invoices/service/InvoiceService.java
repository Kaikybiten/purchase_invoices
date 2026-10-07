package com.example.purchase_invoices.service;

import com.example.purchase_invoices.model.Invoice;
import com.example.purchase_invoices.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public boolean existsByAccessToken(String acessToken) {
        return invoiceRepository.existsByAccessToken(acessToken);
    }
}
