package antonchuvashov.familybudget;

import antonchuvashov.daopost.*;
import antonchuvashov.model.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class MainController {

    @FXML
    private void handleAddIncome() {
        System.out.println("Добавить доход");
    }

    @FXML
    private void handleAddExpense() {
        System.out.println("Добавить расход");
    }

    @FXML
    private void handleViewStatistics() {
        System.out.println("Посмотреть статистику");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label totalExpenseLabel;

    @FXML
    private VBox transactionList;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> entryComboBox;

    @FXML
    public void initialize() {
        // Устанавливаем текущий месяц по умолчанию
        startDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
        endDatePicker.setValue(LocalDate.now());
        loadCategories();
        loadEntries();
        refreshData();
    }

    // Загрузка категорий
    private void loadCategories() {
        List<Category> categories;
        try {
            categories = CategoryDAO.getAllCategories();
            categoryComboBox.getItems().add("Все");
            for (Category category : categories) {
                categoryComboBox.getItems().add(category.getName());
            }
        } catch (SQLException e) {
            showError("Не удалось получить катеогрии доходов.");
        }
        categoryComboBox.setValue("Все");
    }

    // Загрузка статей
    private void loadEntries() {
        List<Entry> entries;
        try {
            entries = EntryDAO.getAllEntries();
            entryComboBox.getItems().add("Все");
            for (Entry entry : entries) {
                entryComboBox.getItems().add(entry.getName());
            }
        } catch (SQLException e) {
            showError("Не удалось получить статьи расходов.");
        }
        entryComboBox.setValue("Все");
    }

    @FXML
    private void handleRefresh() {
        refreshData();
    }

    private void refreshData() {
        transactionList.getChildren().clear();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showError("Пожалуйста, выберите временной промежуток.");
            return;
        }

        String categoryFilter = categoryComboBox.getValue().equals("Все") ? null : categoryComboBox.getValue();
        String entryFilter = entryComboBox.getValue().equals("Все") ? null : entryComboBox.getValue();
        String personFilter = null;

        // Загрузка данных
        List<TransactionRecord> transactions = new ArrayList<>();
        transactions.addAll(IncomeDAO.fetchIncomes(startDate, endDate, categoryFilter, personFilter));
        transactions.addAll(ExpenseDAO.fetchExpenses(startDate, endDate, entryFilter, personFilter));

        // Сортировка по дате
        transactions.sort(Comparator.comparing(TransactionRecord::getDate));

        // Отображение транзакций
        for (TransactionRecord transaction : transactions) {
            totalIncome = totalIncome.add(Objects.equals(transaction.getType(), "income") ? transaction.getAmount() : BigDecimal.ZERO);
            totalExpense = totalExpense.add(Objects.equals(transaction.getType(), "expense") ? transaction.getAmount() : BigDecimal.ZERO);
            addTransactionToList(
                    transaction.getAmount(),
                    transaction.getName(),
                    transaction.getDate().toString(),
                    transaction.getColor(),
                    transaction.getUser()
            );
        }

        totalIncomeLabel.setText(String.format("%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("%.2f", totalExpense));
    }


    private void addTransactionToList(BigDecimal amount, String category, String date, String color, String user) {
        // Сумма
        Label amountLabel = new Label(String.format("%+.2f", amount));
        amountLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " + color + ";");

        // Категория
        Label categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Дата и пользователь
        Label dateLabel = new Label(date + " • " + user);
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Контейнер для одной транзакции
        VBox transactionBox = new VBox(amountLabel, categoryLabel, dateLabel);
        transactionBox.setSpacing(2);
        transactionBox.setAlignment(Pos.BASELINE_LEFT);

        transactionList.getChildren().add(transactionBox);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
