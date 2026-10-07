package com.example.purchase_invoices.controller;

import com.example.purchase_invoices.dto.InvoiceResponse;
import com.example.purchase_invoices.model.InvoiceUrlRequest;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.repository.InvoiceRepository;
import com.example.purchase_invoices.response.JsonResponse;
import com.example.purchase_invoices.scrapper.ReaderHTML;
import com.example.purchase_invoices.service.InvoiceService;
import com.example.purchase_invoices.service.RecordedPurchasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class RecordedPurchasesController {

    @Autowired
    private RecordedPurchasesService recordedPurchasesService;

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<JsonResponse<InvoiceResponse>> postProduct(
            @RequestBody InvoiceUrlRequest invoiceUrlRequest
    ) {

        List<RecordedPurchases> purchasesList = ReaderHTML.getProducts(invoiceUrlRequest.getUrl());

        String accessToken = purchasesList.get(0).getInvoice().getAccessToken().trim();

        if (invoiceService.existsByAccessToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    JsonResponse.error("A nota informada já foi registrada.")
            );
        }

        if (purchasesList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    JsonResponse.error("Nenhum produto localizado na nota informada.")
            );
        }

        List<RecordedPurchases> savedPurchasesList = recordedPurchasesService.saveValidProduct(purchasesList);

        InvoiceResponse response = recordedPurchasesService.toResponse(savedPurchasesList);


        return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.success(response));
    }

}
