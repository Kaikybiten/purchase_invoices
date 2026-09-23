package com.example.purchase_invoices;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReaderHTML {

    private static String readTd(Element tr, String query) {

        return Objects.requireNonNull(
                tr.select(query).first()
            ).text().trim();
    }

    private static String validateRef(Element tr, String query) {
        return  readTd(tr, query).split(":")[1].trim();
    }

    private static Double convertedDouble (String value) {
        return Double.parseDouble(
                value.replace(",", ".")
        );
    }



    public static List<RecordedPurchases> getProducts(String url) {
        List<RecordedPurchases> recordedPurchases = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();

            Element element = document.getElementById("tabResult");

            assert element != null;
            Elements elements = element.select("tr");

            for (Element tr : elements) {

                String code = readTd(tr,"span.RCod").replaceAll("[^0-9]", "").trim();

                String name = readTd(tr, "span.txtTit");
                String measure = validateRef(tr, "span.RUN");

                double unitPrice = convertedDouble( validateRef(tr, "span.RvlUnit") );

                double quantity = convertedDouble( validateRef(tr, "span.Rqtd") );
                double totalPrice = convertedDouble( readTd(tr, "span.valor") );

                System.out.println("-----------");
                System.out.println("Name: " + name);
                System.out.println("Code: " + code);
                System.out.println("Measure: " + measure);
                System.out.println("UnitPrice: " + unitPrice);

                System.out.println("Quantity: " + quantity);
                System.out.println("TotalPrice: " + totalPrice);
            }

        } catch (IOException erro) {
            erro.printStackTrace();

        } finally {
            return recordedPurchases;
        }
    }




}
