package net.cdahmedeh.ultimeter.persistence.manager;

import java.sql.SQLException;

import lombok.Getter;
import net.cdahmedeh.ultimeter.domain.Todo;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Used for managing connections to database and generating database access 
 * objects.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class PersistenceManager {
    private static final String DATABASE_LOCATION = "C:/Users/cdahmedeh/Personal/Sandbox/Databases/ultimeter_new";
    private static final String CONFIGURATION = "AUTO_SERVER=TRUE";
    private static final String JDBC_URL = "jdbc:h2:" + DATABASE_LOCATION + ";" + CONFIGURATION;

    @Getter
    private ConnectionSource connectionSource;

    @Getter
    private Dao<Todo, Long> todoDao;

    public PersistenceManager() throws SQLException {
        // Build connection pool
        connectionSource = new JdbcConnectionSource(JDBC_URL);

        // Setup tables for domain objects and initialize their ORM DAO.
        TableUtils.createTableIfNotExists(connectionSource, Todo.class);
        todoDao = DaoManager.createDao(connectionSource, Todo.class);
    }
}