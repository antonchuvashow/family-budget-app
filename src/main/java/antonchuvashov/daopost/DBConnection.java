package antonchuvashov.daopost;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String url;
    private static String user;
    private static String password;
    private static Connection connection;

    private DBConnection() {
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
        return connection;
    }
}
