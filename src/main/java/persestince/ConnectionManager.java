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
    public static final String SINGLETON_CLONING_NOT_SUPPORTED = "Singleton cloning not supported.";
    private ConnectionManager connectionManager;

    private static final String PATH_TO_PROPERTIES = "src/main/resources/app.properties";
    private static final String DB_DRIVER = "DB_DRIVER";
    private static final String DB_URL = "DB_URL";
    private static final String DB_USER = "DB_USER";
    private static final String DB_PASSWORD = "DB_PASSWORD";

    public static synchronized ConnectionManager getConnectionManager(){
        return new ConnectionManager();
    }

    private ConnectionManager() {
    }

    private Properties loadApplicationProperties() throws IOException {
        final Properties properties = new Properties();
        properties.load(new FileInputStream(new File(PATH_TO_PROPERTIES)));

        return properties;
    }

    public Connection getConnection() {

        try {
            final Properties properties = loadApplicationProperties();
            Class.forName(properties.getProperty(DB_DRIVER));

            return DriverManager.getConnection(
                    properties.getProperty(DB_URL),
                    properties.getProperty(DB_USER),
                    properties.getProperty(DB_PASSWORD));
        } catch (final ClassNotFoundException | SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        throw new CloneNotSupportedException(SINGLETON_CLONING_NOT_SUPPORTED);
    }
}
