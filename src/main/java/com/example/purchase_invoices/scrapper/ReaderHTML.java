package scrapper;

import model.RecordedPurchases;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    private static Double convertedDouble (String value) {
        return Double.parseDouble(
                value.replace(",", ".")
        );
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

    public static void getProducts(String url) {
        List<RecordedPurchases> recordedPurchases = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();

            Element element = document.getElementById("tabResult");

            assert element != null;
            Elements elements = element.select("tr");

            for (Element tr : elements) {

                String code = validateRefCode(tr);

                String name = readTd(tr, "span.txtTit");
                String measure = validateRefMeasurement(tr);

                double unitPrice = convertedDouble( validateRefAmount(tr) );

                double quantity = convertedDouble( validateRefQuantity(tr) );
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

        }
    }

}
