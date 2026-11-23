package com.example.purchase_invoices.scrapper;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    BigDecimal convertedDecimal (String value) {
        return new BigDecimal(value.trim().replace(",", "."));
    }

    Matcher validateMatcher(String td, String stringPattern) {

        Pattern pattern = Pattern.compile(stringPattern);
        return  pattern.matcher(td);

    }
}
