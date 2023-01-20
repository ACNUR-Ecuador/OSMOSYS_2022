package com.sagatechs.generics.utils;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.threeten.extra.YearQuarter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
@Named
@ApplicationScoped
public class DateUtils {
    private static final Logger LOGGER = Logger.getLogger(DateUtils.class);
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

        if (day == null) return null;
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

    public List<YearQuarter> calculateQuarter(LocalDate startDate, LocalDate endDate) {
        List<YearQuarter> yqs = new ArrayList<>();
        YearQuarter yqStart = YearQuarter.from(startDate);
        YearQuarter yqStop = YearQuarter.from(endDate);
        YearQuarter yq = yqStart;
        while (yq.isBefore(yqStop.plusQuarters(1))) {  // Using Half-Open approach where the beginning is *inclusive* while the ending is *exclusive*.
            yqs.add(yq);  // Collect this quarter.
            // Set up next loop.
            yq = yq.plusQuarters(1);  // Move to next quarter.
        }
        return yqs;
    }

    public List<YearMonth> calculateYearMonthsBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<YearMonth> yearMonths = new ArrayList<>();
        YearMonth ymStart = YearMonth.from(startDate);
        YearMonth ymStop = YearMonth.from(endDate);
        YearMonth ym = ymStart;
        while (ym.isBefore(ymStop.plusMonths(1))) {  // Using Half-Open approach where the beginning is *inclusive* while the ending is *exclusive*.
            yearMonths.add(ym);  // Collect this quarter.
            // Set up next loop.
            ym = ym.plusMonths(1);  // Move to next quarter.
        }
        return yearMonths;
    }

    public boolean checkBetweenInclusive(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
        return !dateToCheck.isBefore(startDate) && !dateToCheck.isAfter(endDate);
    }

}
