package antonchuvashov.familybudget;

import antonchuvashov.daopost.*;
import antonchuvashov.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class MainController {

    @FXML
    public ComboBox<String> userComboBox;

    @FXML
    private void handleAddIncomeCategory() {
        openEditorWindow(IncomeCategoryDAO.getInstance());
    }

    @FXML
    private void handleAddExpenseCategory() {
        openEditorWindow(ExpenseCategoryDAO.getInstance());
    }

    private void openEditorWindow(CategoryDAO dao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Editor.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Редактор категорий");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            // Передаём контекст в контроллер
            EditorController controller = loader.getController();
            controller.setContext("Редактор категорий", dao);

            stage.showAndWait();
        } catch (IOException e) {
            LoginApp.showError("Не удалось открыть окно редактора.");
        }
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
        loadCategories(categoryComboBox);
        loadCategories(entryComboBox);
        loadUsers();
        refreshData();

        userComboBox.setDisable(!AuthenticationState.getInstance().isAdmin());
    }

    // Загрузка категорий
    private void loadCategories(ComboBox<String> comboBox) {
        List<GeneralCategory> categories;
        try {
            categories = IncomeCategoryDAO.getInstance().getAll();
            comboBox.getItems().add("Все");
            for (GeneralCategory category : categories) {
                comboBox.getItems().add(category.getName());
            }
        } catch (SQLException e) {
            LoginApp.showError("Не удалось получить катеогрии.");
        }
        comboBox.setValue("Все");
    }

    private void loadUsers() {
        List<User> users;
        try {
            users = UserDAO.getAll();
            for (User user : users) {
                userComboBox.getItems().add(user.getUsername());
            }
        } catch (SQLException e) {
            LoginApp.showError("Не удалось получить ползователей.");
        }
        userComboBox.setValue(AuthenticationState.getInstance().getUsername());
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
            LoginApp.showError("Пожалуйста, выберите временной промежуток.");
            return;
        }

        String categoryFilter = categoryComboBox.getValue().equals("Все") ? null : categoryComboBox.getValue();
        String entryFilter = entryComboBox.getValue().equals("Все") ? null : entryComboBox.getValue();
        String personFilter = userComboBox.getValue();

        // Загрузка данных
        List<TransactionRecord> transactions = new ArrayList<>();
        try {
            transactions.addAll(IncomeDAO.fetch(startDate, endDate, categoryFilter, personFilter));
        } catch (SQLException e) {
            LoginApp.showError("Не удалось получить доходы из базы данных.");
        }

        try {
            transactions.addAll(ExpenseDAO.fetch(startDate, endDate, entryFilter, personFilter));
        } catch (SQLException e) {
            LoginApp.showError("Не удалось получить расходы из базы данных.");
        }

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
}
