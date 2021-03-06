package persestince;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for get connection with data base
 */
public class ConnectionManager {
    private static final ConnectionManager connectionManager = new ConnectionManager();

    private Connection connection;

    private static final String SINGLETON_CLONING_NOT_SUPPORTED = "Singleton cloning not supported.";
    private static final String PATH_TO_PROPERTIES = "src/main/resources/app.properties";
    private static final String DB_DRIVER = "DB_DRIVER";
    private static final String DB_URL = "DB_URL";
    private static final String DB_USER = "DB_USER";
    private static final String DB_PASSWORD = "DB_PASSWORD";

    /**
     * Gets instance of the ConnectionManager
     *
     * @return ConnectionManager
     */
    public static synchronized ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    private ConnectionManager() {
    }

    /**
     * Load application properties from the PATH_TO_PROPERTIES file
     *
     * @return Properties
     * @throws IOException
     */
    private Properties loadApplicationProperties() throws IOException {
        final Properties properties = new Properties();
        properties.load(new FileInputStream(new File(PATH_TO_PROPERTIES)));

        return properties;
    }

    /**
     * Open connection to the data base and provide it.
     *
     * @return Connection
     */
    public Connection getConnection() {

        try {
            final Properties properties = loadApplicationProperties();
            Class.forName(properties.getProperty(DB_DRIVER));

            connection = DriverManager.getConnection(
                    properties.getProperty(DB_URL),
                    properties.getProperty(DB_USER),
                    properties.getProperty(DB_PASSWORD));

            return connection;

        } catch (final ClassNotFoundException | SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    /**
     * Close connection if not closed.
     */
    public void closeConnection() {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disable clone possibility.
     *
     * @return Object
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException(SINGLETON_CLONING_NOT_SUPPORTED);
    }
}
