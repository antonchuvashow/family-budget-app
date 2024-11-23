package antonchuvashov.daopost;

import antonchuvashov.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO CATEGORY (category_id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, category.getCategoryId());
            preparedStatement.setString(2, category.getName());
            preparedStatement.executeUpdate();
        }
    }

    public static Category getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT * FROM CATEGORY WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("name")
                );
            }
        }
        return null;
    }

    public static List<Category> getAllCategories() throws SQLException {
        String query = "SELECT * FROM CATEGORY";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                categories.add(new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("name")
                ));
            }
        }
        return categories;
    }

    public static void updateCategory(Category category) throws SQLException {
        String query = "UPDATE CATEGORY SET name = ? WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getCategoryId());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM CATEGORY WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        }
    }
}
