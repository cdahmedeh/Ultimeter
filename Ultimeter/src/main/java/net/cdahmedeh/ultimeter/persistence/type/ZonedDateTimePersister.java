package net.cdahmedeh.ultimeter.persistence.type;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * Converts between ZonedDateTime and SQL TIMESTAMP formats. Tries to allow
 * ORM to deal with date types through the database for comparison. 
 * 
 * TODO: Time zone particularities are not dealt with.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class ZonedDateTimePersister extends BaseDataType {
    private static final String SQL_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern(SQL_DATE_FORMAT);
    
    @Getter
    private static final ZonedDateTimePersister singleton = new ZonedDateTimePersister();
    
    protected ZonedDateTimePersister() {
        super(SqlType.DATE, new Class<?>[]{ZonedDateTime.class});
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        return SQL_FORMATTER.parse(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getTimestamp(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        Instant instant = ((Timestamp)sqlArg).toInstant();
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    
    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        ZonedDateTime dateTime = (ZonedDateTime) javaObject;
        return Timestamp.from(dateTime.toInstant());
    }
    
    @Override
    public boolean isComparable() {
        return true;
    }

}
