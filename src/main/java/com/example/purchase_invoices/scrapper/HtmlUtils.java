package com.example.purchase_invoices.scrapper;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    BigDecimal convertedDecimal (String value) {
        return new BigDecimal(value.trim().replace(",", "."));
    }

    Matcher validateMatcher(String td, String patern) {

        Pattern pattern = Pattern.compile(patern);
        return  pattern.matcher(td);

    }
}
