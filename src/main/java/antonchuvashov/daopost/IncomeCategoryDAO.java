package antonchuvashov.daopost;

import antonchuvashov.model.GeneralCategory;
import antonchuvashov.model.IncomeCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeCategoryDAO implements CategoryDAO {

    private static final class InstanceHolder {
        private static final IncomeCategoryDAO instance = new IncomeCategoryDAO();
    }

    public static IncomeCategoryDAO getInstance() {
        return IncomeCategoryDAO.InstanceHolder.instance;
    }

    @Override
    public void add(GeneralCategory generalCategory) throws SQLException {
        String query = "INSERT INTO CATEGORY (category_id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, generalCategory.getId());
            preparedStatement.setString(2, generalCategory.getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void add(String name) throws SQLException {
        String query = "INSERT INTO CATEGORY (name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }

    public IncomeCategory get(int categoryId) throws SQLException {
        String query = "SELECT * FROM CATEGORY WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new IncomeCategory(
                        resultSet.getInt("category_id"),
                        resultSet.getString("name")
                );
            }
        }
        return null;
    }

    public List<GeneralCategory> getAll() throws SQLException {
        String query = "SELECT * FROM CATEGORY";
        List<GeneralCategory> categories = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                categories.add(new IncomeCategory(
                        resultSet.getInt("category_id"),
                        resultSet.getString("name")
                ));
            }
        }
        return categories;
    }

    @Override
    public void update(int id, String newName) throws SQLException {
        String query = "UPDATE CATEGORY SET name = ? WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int categoryId) throws SQLException {
        String query = "DELETE FROM CATEGORY WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        }
    }
}
