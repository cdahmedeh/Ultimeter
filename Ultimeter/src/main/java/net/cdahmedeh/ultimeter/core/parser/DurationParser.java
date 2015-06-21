package net.cdahmedeh.ultimeter.core.parser;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationParser {
    /**
     * Converts duration to human-readable string. A blank string is returned 
     * for null.
     * 
     * @param duration The duration to convert.
     * @return A human readable string. 
     */
    public static String unparse(Duration duration) {
        if (duration == null) {
            return "";
        }
        return duration.toString();
    }
    
    /**
     * Parses text into Duration.
     * 
     * @param text The text to parse.
     * @return The resulting duration from the parsing. Null if fails.
     */
    public static Duration parse(String text) {
        if (text.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Duration.parse(text);    
        } catch (DateTimeParseException e) {
            
        }
        
        return null;
    }
}
