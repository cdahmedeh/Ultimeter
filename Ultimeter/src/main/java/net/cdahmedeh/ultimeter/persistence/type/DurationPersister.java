package net.cdahmedeh.ultimeter.persistence.type;

import java.sql.SQLException;
import java.time.Duration;
import java.time.ZonedDateTime;

import lombok.Getter;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * Converts between Duration and SQL LONG formats. Tries to allow
 * ORM to deal with long types through the database for comparison. 
 * 
 * Durations are stored as their seconds length.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class DurationPersister extends BaseDataType {
    @Getter
    private static final DurationPersister singleton = new DurationPersister();
    
    protected DurationPersister() {
        super(SqlType.LONG, new Class<?>[]{ZonedDateTime.class});
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        return Duration.ofSeconds(Long.parseLong(defaultStr));
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getLong(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        Long seconds = (Long) sqlArg;
        return Duration.ofSeconds(seconds);
    }
    
    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        Duration duration = (Duration) javaObject;
        return duration.getSeconds();
    }
    
    @Override
    public boolean isComparable() {
        return true;
    }

}
