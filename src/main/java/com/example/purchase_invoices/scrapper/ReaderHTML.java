package com.example.purchase_invoices.scrapper;

import com.example.purchase_invoices.exception.InvoiceReadException;
import com.example.purchase_invoices.model.Invoice;
import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class ReaderHTML {

    private static final HtmlUtils htmlUtils = new HtmlUtils();
    private static final HtmlExtractor htmlExtractor = new HtmlExtractor();

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public static List<RecordedPurchases> getProducts(String url) {

        List<RecordedPurchases> recordedPurchases = new ArrayList<>();

        try {

            String encodedUrl = url.replace("|", "%7C");

            // Contrutor
            HttpRequest.Builder builder = HttpRequest.newBuilder();

            URI urlAddress = URI.create(encodedUrl);

            // Requisição
            HttpRequest request = builder
                    .uri(urlAddress) // Endereço
                    .header("User-Agent", "Mozilla/5.0") // Dados genericos para entrada
                    .header("Accept", "text/html,application/xhtml+xml")
                    .GET() // Define o metodo da requisição
                    .build(); // Constroi a requisição

            // Executa requisição
            HttpResponse<String> response = CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofString() // Define como resposta será tratada
            );

            if (response.statusCode() != 200) {
                throw new InvoiceReadException(
                        "Não foi possível consultar a NFC-e. HTTP Status: " + response.statusCode()
                );
            }

            Document document = Jsoup.parse(response.body());

            Element element = document.getElementById("tabResult");

            if (element == null){
                return recordedPurchases;
            }

            Elements elements = element.select("tr");

            LocalDate purchaseDate = htmlExtractor.convertDate(document);
            String purchaseCode =  htmlExtractor.getCode(document);

            Invoice invoice = new Invoice(purchaseCode, purchaseDate);

            for (Element tr : elements) {

                String name = htmlExtractor.readTd(tr, "span.txtTit");
                String code = htmlExtractor.validateRefCode(tr);
                String measure = htmlExtractor.validateRefMeasurement(tr);
                BigDecimal unitPrice = htmlUtils.convertedDecimal(htmlExtractor.validateRefAmount(tr));

                BigDecimal quantity = htmlUtils.convertedDecimal(htmlExtractor.validateRefQuantity(tr));
                BigDecimal totalPrice = htmlUtils.convertedDecimal( htmlExtractor.readTd(tr, "span.valor") );

                BigDecimal allegedTotalPrice = unitPrice
                        .multiply(quantity)
                        .setScale(2, RoundingMode.HALF_UP);

                if (allegedTotalPrice.compareTo(totalPrice) != 0) {
                    continue;
                }

                Product product = new Product(name, code, measure, unitPrice);

                recordedPurchases.add(
                        new RecordedPurchases(invoice, product, totalPrice, quantity, purchaseDate)
                );
            }

        } catch (IOException | InterruptedException erro) {

            System.err.println("Erro original: " + erro.getClass().getName());
            erro.printStackTrace();

            throw new InvoiceReadException(
                    "Não foi possível realizar a leitura da nota fiscal informada",
                    erro
            );
        }
        return recordedPurchases;
    }

}
