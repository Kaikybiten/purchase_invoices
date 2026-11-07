package com.example.purchase_invoices.scrapper;

import com.example.purchase_invoices.exception.InvoiceReadException;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;

public class HtmlExtractor {

    private final HtmlUtils htmlUtils = new HtmlUtils();

    String readTd(Element tr, String query) {

        return Objects.requireNonNull(
                tr.select(query).first()
        ).text().trim();
    }

    String validateRefAmount(Element tr) {

        String td = readTd(tr, "span.RvlUnit");
        if (td.isEmpty()) return "0";

        Matcher matcher = htmlUtils.validateMatcher(td, "(\\d+),(\\d{1,2})");

        if (matcher.find())  return matcher.group();

        return "0";

    }

    String validateRefQuantity (Element tr) {

        String td = readTd(tr, "span.Rqtd");
        if (td.isEmpty()) return "0";

        Matcher matcher = htmlUtils.validateMatcher(td, "(\\d+)(,)?(\\d+)?");

        if (matcher.find())  {
            return matcher.group();
        }

        return "0";
    }

    String validateRefMeasurement (Element tr) {

        String td = readTd(tr, "span.RUN");
        if (td.isEmpty()) return "UN";

        return td.split(":")[1].trim();
    }

    String validateRefCode(Element tr) {
        String td = readTd(tr, "span.RCod");

        // (.*?) = Obtem todos os caracteres entre : e ), '?' garante uma unica ocorrencia
        Matcher matcher = htmlUtils.validateMatcher(td, "\\:(.*?)\\)");

        if (matcher.find())  return matcher.group(1).trim();

        return "0";
    }

    LocalDate convertDate(Element doc) {

        String dateText = readTd(doc, "ul.jqm-listview");

        Matcher matcher = htmlUtils.validateMatcher(
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

        return LocalDateTime
                .parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                .toLocalDate();
    }

    String getCode(Element doc) {
        String accessKey =readTd(doc, "ul.jqm-listview span.chave");
        return accessKey.replaceAll("\\s+", "");
    }

}
