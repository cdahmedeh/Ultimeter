package net.cdahmedeh.ultimeter.core.parser;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

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
        return dateTime.toString();
    }
    
    /**
     * Parses date from human-readable format. An invalid format will return
     * null.
     * 
     * @param text The text to parse.
     * @return A parsed date. Null for invalid format.
     */
    public static ZonedDateTime parse(String text) {
        try {
            return ZonedDateTime.parse(text);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
