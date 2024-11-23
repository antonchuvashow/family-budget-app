package antonchuvashov.daopost;

import antonchuvashov.model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public static void addExpense(Expense expense) throws SQLException {
        String query = "INSERT INTO EXPENSE (expense_id, user_id, amount, operation_date, entry_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, expense.getExpenseId());
            preparedStatement.setString(2, expense.getUserId());
            preparedStatement.setBigDecimal(3, expense.getAmount());
            preparedStatement.setDate(4, new java.sql.Date(expense.getOperationDate().getTime()));
            preparedStatement.setInt(5, expense.getEntryId());
            preparedStatement.executeUpdate();
        }
    }

    public static Expense getExpenseById(int expenseId) throws SQLException {
        String query = "SELECT * FROM EXPENSE WHERE expense_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, expenseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Expense(
                        resultSet.getInt("expense_id"),
                        resultSet.getString("user_id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getDate("operation_date"),
                        resultSet.getInt("entry_id")
                );
            }
        }
        return null;
    }

    public static List<Expense> getAllExpenses() throws SQLException {
        String query = "SELECT * FROM EXPENSE";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                expenses.add(new Expense(
                        resultSet.getInt("expense_id"),
                        resultSet.getString("user_id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getDate("operation_date"),
                        resultSet.getInt("entry_id")
                ));
            }
        }
        return expenses;
    }

    public static void updateExpense(Expense expense) throws SQLException {
        String query = "UPDATE EXPENSE SET user_id = ?, amount = ?, operation_date = ?, entry_id = ? WHERE expense_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, expense.getUserId());
            preparedStatement.setBigDecimal(2, expense.getAmount());
            preparedStatement.setDate(3, new java.sql.Date(expense.getOperationDate().getTime()));
            preparedStatement.setInt(4, expense.getEntryId());
            preparedStatement.setInt(5, expense.getExpenseId());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteExpense(int expenseId) throws SQLException {
        String query = "DELETE FROM EXPENSE WHERE expense_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, expenseId);
            preparedStatement.executeUpdate();
        }
    }
}