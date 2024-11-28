package antonchuvashov.daopost;

import antonchuvashov.model.Income;
import antonchuvashov.model.IncomeCategory;
import antonchuvashov.model.TransactionRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    public static void add(Income income) throws SQLException {
        String query = "INSERT INTO INCOME (user_id, amount, operation_date, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, income.getUser());
            preparedStatement.setBigDecimal(2, income.getAmount());
            preparedStatement.setDate(3, java.sql.Date.valueOf(income.getDate()));
            preparedStatement.setInt(4, income.getCategory().getId());
            preparedStatement.executeUpdate();
        }
    }

    public static void update(Income income) throws SQLException {
        String query = "UPDATE INCOME SET user_id = ?, amount = ?, operation_date = ?, category_id = ? WHERE income_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, income.getUser());
            preparedStatement.setBigDecimal(2, income.getAmount());
            preparedStatement.setDate(3, java.sql.Date.valueOf(income.getDate()));
            preparedStatement.setInt(4, income.getCategory().getId());
            preparedStatement.setInt(5, income.getIncomeId());
            preparedStatement.executeUpdate();
        }
    }

    public static void delete(int incomeId) throws SQLException {
        String query = "DELETE FROM INCOME WHERE income_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, incomeId);
            preparedStatement.executeUpdate();
        }
    }

    public static List<TransactionRecord> fetch(LocalDate startDate, LocalDate endDate, String categoryFilter, String personFilter) throws SQLException {
        List<TransactionRecord> incomes = new ArrayList<>();
        PreparedStatement stmt = getPreparedStatement(categoryFilter, personFilter);
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
                    result.getString("user_id"),
                    result.getBigDecimal("amount"),
                    result.getDate("operation_date").toLocalDate(),
                    new IncomeCategory(result.getInt("category_id"),
                            result.getString("category"))
            ));
        }
        return incomes;
    }

    private static PreparedStatement getPreparedStatement(String categoryFilter, String personFilter) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT i.income_id, i.amount, c.name, i.user_id, c.category_id, c.name AS category, i.operation_date, u.full_name " +
                "FROM income i " +
                "JOIN category c ON i.category_id = c.category_id " +
                "JOIN users u ON i.user_id = u.username " +
                "WHERE i.operation_date BETWEEN ? AND ? " +
                (categoryFilter != null ? "AND c.name = ? " : "") +
                (personFilter != null ? "AND i.user_id = ? " : "");
        return connection.prepareStatement(query);
    }
}
