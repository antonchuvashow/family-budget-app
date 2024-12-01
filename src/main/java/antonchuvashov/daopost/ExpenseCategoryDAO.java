package antonchuvashov.daopost;

import antonchuvashov.model.ExpenseCategory;
import antonchuvashov.model.GeneralCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCategoryDAO implements CategoryDAO{

    private static final class InstanceHolder {
        private static final ExpenseCategoryDAO instance = new ExpenseCategoryDAO();
    }

    public static ExpenseCategoryDAO getInstance() {
        return InstanceHolder.instance;
    }

    public void add(ExpenseCategory expenseCategory) throws SQLException {
        String query = "INSERT INTO ENTRY (entry_id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, expenseCategory.getId());
            preparedStatement.setString(2, expenseCategory.getName());
            preparedStatement.executeUpdate();
        }
    }

    public ExpenseCategory get(int id) throws SQLException {
        String query = "SELECT * FROM ENTRY WHERE entry_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new ExpenseCategory(
                        resultSet.getInt("entry_id"),
                        resultSet.getString("name")
                );
            }
        }
        return null;
    }

    public ExpenseCategory get(String entryName) throws SQLException {
        String query = "SELECT * FROM ENTRY WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, entryName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new ExpenseCategory(
                        resultSet.getInt("entry_id"),
                        resultSet.getString("name")
                );
            }
        }
        return null;
    }

    public List<GeneralCategory> getAll() throws SQLException {
        String query = "SELECT * FROM ENTRY";
        List<GeneralCategory> entries = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                entries.add(new ExpenseCategory(
                        resultSet.getInt("entry_id"),
                        resultSet.getString("name")
                ));
            }
        }
        return entries;
    }

    @Override
    public void add(GeneralCategory generalCategory) throws SQLException {
        String query = "INSERT INTO ENTRY (entry_id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, generalCategory.getId());
            preparedStatement.setString(2, generalCategory.getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void add(String name) throws SQLException {
        String query = "INSERT INTO ENTRY (name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }

    public void update(int id, String newName) throws SQLException {
        String query = "UPDATE ENTRY SET name = ? WHERE entry_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM ENTRY WHERE entry_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
