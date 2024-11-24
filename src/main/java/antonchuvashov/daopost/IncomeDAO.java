package antonchuvashov.daopost;

import antonchuvashov.model.Expense;
import antonchuvashov.model.Income;
import antonchuvashov.model.TransactionRecord;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    public static void addIncome(Income income) throws SQLException {
        String query = "INSERT INTO INCOME (income_id, user_id, amount, operation_date, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, income.getIncomeId());
            preparedStatement.setString(2, income.getUser());
            preparedStatement.setBigDecimal(3, income.getAmount());
            preparedStatement.setDate(4, new java.sql.Date(income.getDate().getTime()));
            preparedStatement.setInt(5, income.getCategoryId());
            preparedStatement.executeUpdate();
        }
    }

    public static void updateIncome(Income income) throws SQLException {
        String query = "UPDATE INCOME SET user_id = ?, amount = ?, operation_date = ?, category_id = ? WHERE income_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, income.getUser());
            preparedStatement.setBigDecimal(2, income.getAmount());
            preparedStatement.setDate(3, new java.sql.Date(income.getDate().getTime()));
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

    public static List<TransactionRecord> fetchIncomes(LocalDate startDate, LocalDate endDate, String categoryFilter, String personFilter) {
        List<TransactionRecord> incomes = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT i.income_id, i.amount, c.name AS category, c.category_id, i.operation_date, u.full_name " +
                    "FROM income i " +
                    "JOIN category c ON i.category_id = c.category_id " +
                    "JOIN users u ON i.user_id = u.username " +
                    "WHERE i.operation_date BETWEEN ? AND ? " +
                    (categoryFilter != null ? "AND c.name = ? " : "") +
                    (personFilter != null ? "AND u.full_name = ? " : "");
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            int paramIndex = 3;

            if (categoryFilter != null) {
                stmt.setString(paramIndex++, categoryFilter);
            }
            if (personFilter != null) {
                stmt.setString(paramIndex, personFilter);
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                incomes.add(new Income(
                        result.getInt("income_id"),
                        result.getString("full_name"),
                        result.getBigDecimal("amount"),
                        result.getDate("operation_date"),
                        result.getInt("category_id"),
                        result.getString("category")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }
}
