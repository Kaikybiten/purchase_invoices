package com.example.purchase_invoices;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.purchase_invoices.scrapper.ReaderHTML;

@SpringBootApplication
public class PurchaseInvoicesApplication {

	public static void main(String[] args) {

		String url = "https://www.nfce.fazenda.sp.gov.br/NFCeConsultaPublica/Paginas/ConsultaQRCode.aspx?p=35260971779813001818651080001114361002221145|2|1|1|8ee03d5ac0b2f25cd40295ee7f3133957694d578";

		ReaderHTML.getProducts(url);

	}
}