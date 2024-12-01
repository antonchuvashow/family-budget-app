package antonchuvashov.daopost;

import antonchuvashov.model.User;
import antonchuvashov.utils.PasswordUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.*;

public class UserDAO {
    public static void add(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, full_name, birth_date, sum) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setDate(4, new java.sql.Date(user.getBirthday().getTime()));
            preparedStatement.setBigDecimal(5, user.getSum());
            preparedStatement.executeUpdate();
        }
    }

    public static User get(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("full_name"),
                        resultSet.getDate("birth_date")
                );
            } else {
                return null;
            }
        }
    }

    public static ArrayList<User> getAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("full_name"),
                        resultSet.getDate("birth_date")
                ));
            }
        }
        return users;
    }

    public static boolean isOlderThan(String username, int age) throws SQLException {
        User user = get(username);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        Date ageThreshold = Date.valueOf(LocalDate.now().minusYears(age));
        return user.getBirthday().before(ageThreshold);
    }

    public static void update(User user) throws SQLException {
        String query = "UPDATE users SET password = ?, full_name = ?, birth_date = ?, sum = ? WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getPasswordHash());
            statement.setString(2, user.getFullName());
            statement.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            statement.setBigDecimal(4, user.getSum());
            statement.setString(5, user.getUsername());
            statement.executeUpdate();
        }
    }

    public static void delete(String username) throws SQLException {
        String query = "DELETE FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.executeUpdate();
        }
    }

    public static void addRootUserIfDoesntExist() throws SQLException {
        User user = get("root");
        if (user == null || !PasswordUtils.checkPassword("root", user.getPasswordHash())) {
            if (user != null) {
                delete("root");
            }
            user = new User("root", PasswordUtils.hashPassword("root"),
                    "root", Date.valueOf(LocalDate.now().minusYears(20)));
            add(user);
        }
    }
}
