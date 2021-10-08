package com.sagatechs.generics.utils;

import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SuppressWarnings("unused")
@Named
@ApplicationScoped
public class DateUtils {

    private static final String TIME_FORMATTER = "HH:mm";
    private static final String DATE_TIME_FORMATTER = "dd/MM/yyyy HH:mm";
    private static final String DATE_FORMATTER = "dd/MM/yyyy";
    private static final String DATE_POSTGRES_FORMATTER = "YYYY-MM-dd";

    public String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
        return dateTime.format(formatter);

    }

    public String dateAndTimeToString(Date dateTime) {
        if (dateTime == null)
            return "";
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMATTER);
        return dateFormat.format(dateTime);
    }

    public String dateTimeToString(Date dateTime) {
        if (dateTime == null)
            return "";
        DateFormat dateFormat = new SimpleDateFormat(TIME_FORMATTER);
        return dateFormat.format(dateTime);

    }

    public String dateToString(Date dateTime) {
        if (dateTime == null)
            return "";
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMATTER);
        return dateFormat.format(dateTime);

    }

    public Date StringtoDate(String date) throws ParseException {
        if (StringUtils.isBlank(date))
            return null;
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMATTER);
        return dateFormat.parse(date);

    }

    public LocalDate StringtoLocalDate(String date) {
        if (StringUtils.isBlank(date))
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return LocalDate.parse(date, formatter);

    }

    public LocalDateTime StringtoLocalDateTime(String date) {
        if (StringUtils.isBlank(date))
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
        return LocalDateTime.parse(date, formatter);

    }

    public String localDateToString(LocalDate date) {
        if (date == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return date.format(formatter);

    }

    public String localDateTimeToStringPostgres(LocalDate date) {
        if (date == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_POSTGRES_FORMATTER);
        return date.format(formatter);

    }

    public Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String getSpanishDayNameFromDayOfWeek(DayOfWeek day) {

        if(day==null) return null;
        switch (day) {
            case SUNDAY:
                return "Domingo";
            case MONDAY:
                return "Lunes";
            case TUESDAY:
                return "Martes";
            case WEDNESDAY:
                return "Miércoles";
            case THURSDAY:
                return "Jueves";
            case FRIDAY:
                return "Viernes";
            case SATURDAY:
                return "Sábado";
            default:
                return null;
        }

    }

}
