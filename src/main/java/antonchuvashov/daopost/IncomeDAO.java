package antonchuvashov.daopost;

import antonchuvashov.model.Income;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    public static void addIncome(Income income) throws SQLException {
        String query = "INSERT INTO INCOME (income_id, user_id, amount, operation_date, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, income.getIncomeId());
            preparedStatement.setString(2, income.getUserId());
            preparedStatement.setBigDecimal(3, income.getAmount());
            preparedStatement.setDate(4, new java.sql.Date(income.getOperationDate().getTime()));
            preparedStatement.setInt(5, income.getCategoryId());
            preparedStatement.executeUpdate();
        }
    }

    public static Income getIncomeById(int incomeId) throws SQLException {
        String query = "SELECT * FROM INCOME WHERE income_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, incomeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Income(
                        resultSet.getInt("income_id"),
                        resultSet.getString("user_id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getDate("operation_date"),
                        resultSet.getInt("category_id")
                );
            }
        }
        return null;
    }

    public static List<Income> getAllIncomes() throws SQLException {
        String query = "SELECT * FROM INCOME";
        List<Income> incomes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                incomes.add(new Income(
                        resultSet.getInt("income_id"),
                        resultSet.getString("user_id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getDate("operation_date"),
                        resultSet.getInt("category_id")
                ));
            }
        }
        return incomes;
    }

    public static void updateIncome(Income income) throws SQLException {
        String query = "UPDATE INCOME SET user_id = ?, amount = ?, operation_date = ?, category_id = ? WHERE income_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, income.getUserId());
            preparedStatement.setBigDecimal(2, income.getAmount());
            preparedStatement.setDate(3, new java.sql.Date(income.getOperationDate().getTime()));
            preparedStatement.setInt(4, income.getCategoryId());
            preparedStatement.setInt(5, income.getIncomeId());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteIncome(int incomeId) throws SQLException {
        String query = "DELETE FROM INCOME WHERE income_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, incomeId);
            preparedStatement.executeUpdate();
        }
    }
}
