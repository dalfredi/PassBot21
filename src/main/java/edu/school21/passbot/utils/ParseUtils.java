package edu.school21.passbot.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class ParseUtils {
    public static LocalDate parseDate(String dateString) {
        LocalDate date;
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd[.][-][/]MM[.][-][/]yyyy").withResolverStyle(
                        ResolverStyle.SMART
                );
        try {
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            date = null;
        }
        return date;
    }
}
