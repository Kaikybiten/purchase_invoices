package com.example.purchase_invoices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.purchase_invoices.scrapper.ReaderHTML;

@SpringBootApplication
public class PurchaseInvoicesApplication {

	public static void main(String[] args) {

		SpringApplication.run(PurchaseInvoicesApplication.class, args);

	}
}