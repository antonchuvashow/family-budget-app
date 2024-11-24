package antonchuvashov.daopost;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static String url;
    private static String user;
    private static String password;
    private static Connection connection;

    private DBConnection() {
    }

    public static void fromSettings() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("settings")) {
            properties.load(inputStream);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            setConnection(url, user, password);
        }
    }


    public static void setConnection(String url, String username, String password) {
        try {
            DBConnection.url = url;
            DBConnection.user = username;
            DBConnection.password = password;
            connection = DriverManager.getConnection("jdbc:postgresql://" + url, username, password);

        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }

    public static String getDbUrl() {
        return url;
    }

    public static String getDbUsername() {
        return user;
    }

    public static String getDbPassword() {
        return password;
    }

    public static boolean isValidConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Connection getConnection() throws SQLException {
        if (connection.isClosed()) {
            setConnection(DBConnection.url, DBConnection.user, DBConnection.password);
        }
        return connection;
    }
}
