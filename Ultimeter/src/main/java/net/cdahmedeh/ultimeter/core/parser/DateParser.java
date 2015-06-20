package net.cdahmedeh.ultimeter.core.parser;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.CalendarSource;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class DateParser {
    /**
     * Converts date to human-readable string. A blank string is returned for
     * null.
     * 
     * @param dateTime The date to convert.
     * @return A human readable string. 
     */
    public static String unparse(ZonedDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("EEE d MMM"));
    }
    
    /**
     * Parses natural language data into a ZonedDateTime reference. Attempts to
     * use ZonedDateTime default format before trying natural language parsing.
     * If parsing fails, it returns null.
     * 
     * @param text The text to parse.
     * @return The resulting date from the parsing. Null if fails.
     */
    public static ZonedDateTime parse(String text) {
        if (text.trim().isEmpty()) {
            return null;
        }
        
        // Try parsing using default strict parser.
        try {
            return ZonedDateTime.parse(text);    
        } catch (DateTimeParseException e) {
            
        }
        
        // Then attempt natural language parsing.

        // Set default time to midnight if no time is inputted.
        Instant midnight = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).toInstant();
        CalendarSource.setBaseDate(Date.from(midnight));
        
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(text);
        if (false == groups.isEmpty()) {
            List<Date> dates = groups.get(0).getDates();
            if (false == dates.isEmpty()) {
                Instant parsedinstant = dates.get(0).toInstant();
                return ZonedDateTime.ofInstant(parsedinstant, ZoneId.systemDefault());
            }
        }
              
        return null;
    }
}
