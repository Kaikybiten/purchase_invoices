package com.example.purchase_invoices.scrapper;

import com.example.purchase_invoices.exception.InvoiceReadException;
import com.example.purchase_invoices.model.Invoice;
import com.example.purchase_invoices.model.Product;
import com.example.purchase_invoices.model.RecordedPurchases;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cglib.core.Local;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReaderHTML {

    private static Matcher validateMatcher(String td, String patern) {

        Pattern pattern = Pattern.compile(patern);
        return  pattern.matcher(td);

    }

    private static BigDecimal convertedDecimal (String value) {
        return new BigDecimal(value.trim().replace(",", "."));
    }

    private static String readTd(Element tr, String query) {

        return Objects.requireNonNull(
                tr.select(query).first()
            ).text().trim();
    }

    private static String validateRefAmount(Element tr) {

        String td = readTd(tr, "span.RvlUnit");
        if (td.isEmpty()) return "0";

        Matcher matcher = validateMatcher(td, "(\\d+),(\\d{1,2})");

        if (matcher.find())  return matcher.group();

        return "0";

    }

    private static String validateRefQuantity (Element tr) {

        String td = readTd(tr, "span.Rqtd");
        if (td.isEmpty()) return "0";

        Matcher matcher = validateMatcher(td, "(\\d+)(,)?(\\d+)?");

        if (matcher.find())  {
            return matcher.group();
        }

        return "0";
    }

    private static String validateRefMeasurement (Element tr) {

        String td = readTd(tr, "span.RUN");
        if (td.isEmpty()) return "UN";

        return td.split(":")[1].trim();
    }

    private static String validateRefCode(Element tr) {
        String td = readTd(tr, "span.RCod");

        // (.*?) = Obtem todos os caracteres entre : e ), '?' garante uma unica ocorrencia
        Matcher matcher = validateMatcher(td, "\\:(.*?)\\)");

        if (matcher.find())  return matcher.group(1).trim();

        return "0";
    }

    private static LocalDate convertDate(Element doc) {

        String dateText = readTd(doc, "ul.jqm-listview");

        System.out.println(dateText);

        Matcher matcher = validateMatcher(
                dateText,
                "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}"
        );


        if (!matcher.find()) {
            throw new InvoiceReadException(
                    "Não foi possível localizar a data da nota fiscal.",
                    null
            );
        }

        String date = matcher.group();

        System.out.println(date + " " + matcher.group());

        return LocalDateTime
                .parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                .toLocalDate();
    }

    private static String getCode(Element doc) {
        String accessKey =readTd(doc, "ul.jqm-listview span.chave");
        return accessKey.replaceAll("\\s+", "");
    }

    public static List<RecordedPurchases> getProducts(String url) {

        List<RecordedPurchases> recordedPurchases = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();

            Element element = document.getElementById("tabResult");

            if (element == null){
                return recordedPurchases;
            }
            Elements elements = element.select("tr");

            LocalDate purchaseDate = convertDate(document);
            String purchaseCode =  getCode(document);

            Invoice invoice = new Invoice(purchaseCode, purchaseDate);

            for (Element tr : elements) {

                String name = readTd(tr, "span.txtTit");
                String code = validateRefCode(tr);
                String measure = validateRefMeasurement(tr);
                BigDecimal unitPrice = convertedDecimal(validateRefAmount(tr));

                BigDecimal quantity = convertedDecimal(validateRefQuantity(tr));
                BigDecimal totalPrice = convertedDecimal( readTd(tr, "span.valor") );


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

        } catch (IOException erro) {

            throw  new InvoiceReadException(
                    "Não foi possivel realizar a leitura da nota fiscal informada",
                    erro
            );
        }
        return recordedPurchases;
    }

}
