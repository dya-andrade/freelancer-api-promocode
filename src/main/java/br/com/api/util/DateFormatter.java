package br.com.api.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DateFormatter {

    private DateFormatter() {
    }

    public static LocalDateTime dateFormatter(String data) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0).toFormatter();

        return LocalDateTime.parse(data, formatter);
    }
}
