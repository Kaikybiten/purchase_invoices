package com.example.purchase_invoices.controller;

import com.example.purchase_invoices.dto.InvoiceResponse;
import com.example.purchase_invoices.model.InvoiceUrlRequest;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.response.JsonResponse;
import com.example.purchase_invoices.scrapper.ReaderHTML;
import com.example.purchase_invoices.service.InvoiceService;
import com.example.purchase_invoices.service.RecordedPurchasesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class RecordedPurchasesController {

    private final RecordedPurchasesService recordedPurchasesService;

    private final InvoiceService invoiceService;

    public RecordedPurchasesController (
        RecordedPurchasesService recordedPurchasesService,
        InvoiceService invoiceService
    ) {
        this.recordedPurchasesService = recordedPurchasesService;
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<JsonResponse<InvoiceResponse>> postProduct(
            @RequestBody InvoiceUrlRequest invoiceUrlRequest
    ) {

        List<RecordedPurchases> purchasesList = ReaderHTML.getProducts(invoiceUrlRequest.getUrl());

        if (purchasesList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    JsonResponse.error("Nenhum produto localizado na nota informada.")
            );
        }

        String accessToken = purchasesList.getFirst().getInvoice().getAccessToken().trim();

        if (invoiceService.existsByAccessToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    JsonResponse.error("Já existe um nota com esses dados registrada. Verifique se não foi um cadastro duplicado.")
            );
        }

        List<RecordedPurchases> savedPurchasesList = recordedPurchasesService.saveValidProduct(purchasesList);

        InvoiceResponse response = recordedPurchasesService.toResponse(savedPurchasesList);


        return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.success(response));
    }

}
