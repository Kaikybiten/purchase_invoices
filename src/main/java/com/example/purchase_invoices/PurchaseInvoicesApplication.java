package com.example.purchase_invoices;

import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;
import com.example.purchase_invoices.scrapper.ReaderHTML;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PurchaseInvoicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseInvoicesApplication.class, args);
	}
}