package antonchuvashov.daopost;

import antonchuvashov.model.Expense;
import antonchuvashov.model.TransactionRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public static void add(Expense expense) throws SQLException {
        String query = "INSERT INTO EXPENSE (expense_id, user_id, amount, operation_date, entry_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, expense.getExpenseId());
            preparedStatement.setString(2, expense.getUser());
            preparedStatement.setBigDecimal(3, expense.getAmount());
            preparedStatement.setDate(4, new java.sql.Date(expense.getDate().getTime()));
            preparedStatement.setInt(5, expense.getEntryId());
            preparedStatement.executeUpdate();
        }
    }

    public static List<TransactionRecord> fetch(LocalDate startDate, LocalDate endDate, String entryFilter, String personFilter) throws SQLException {
        List<TransactionRecord> expenses = new ArrayList<>();
        PreparedStatement stmt = getPreparedStatement(entryFilter, personFilter);
        stmt.setDate(1, java.sql.Date.valueOf(startDate));
        stmt.setDate(2, java.sql.Date.valueOf(endDate));
        int paramIndex = 3;

        if (entryFilter != null) {
            stmt.setString(paramIndex++, entryFilter);
        }
        if (personFilter != null) {
            stmt.setString(paramIndex, personFilter);
        }

        ResultSet result = stmt.executeQuery();
        while (result.next()) {
            expenses.add(new Expense(
                    result.getInt("expense_id"),
                    result.getString("full_name"),
                    result.getBigDecimal("amount"),
                    result.getDate("operation_date"),
                    result.getInt("entry_id"),
                    result.getString("entry")
            ));
        }
        return expenses;
    }

    private static PreparedStatement getPreparedStatement(String entryFilter, String personFilter) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT ex.expense_id, ex.amount, e.name AS entry,  e.entry_id, ex.operation_date, u.full_name " +
                "FROM expense ex " +
                "JOIN entry e ON ex.entry_id = e.entry_id " +
                "JOIN users u ON ex.user_id = u.username " +
                "WHERE ex.operation_date BETWEEN ? AND ? " +
                (entryFilter != null ? "AND e.name = ? " : "") +
                (personFilter != null ? "AND u.full_name = ? " : "");
        return connection.prepareStatement(query);
    }
}