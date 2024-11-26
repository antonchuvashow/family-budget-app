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
    private TableView<TransactionRecord> transactionTable;

    @FXML
    private TableColumn<TransactionRecord, LocalDate> dateColumn;
    @FXML
    private TableColumn<TransactionRecord, String> categoryColumn;
    @FXML
    private TableColumn<TransactionRecord, BigDecimal> amountColumn;
    @FXML
    private TableColumn<TransactionRecord, String> userColumn;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label totalExpenseLabel;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> entryComboBox;

    @FXML
    public void initialize() {
        // Устанавливаем текущий месяц по умолчанию
        startDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
        endDatePicker.setValue(LocalDate.now());
        loadCategories(IncomeCategoryDAO.getInstance(), categoryComboBox);
        loadCategories(ExpenseCategoryDAO.getInstance(), entryComboBox);
        loadUsers();
        setupTransactionTable();
        refreshData();
    }

    // Настройка таблицы транзакций
    private void setupTransactionTable() {
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        userColumn.setCellValueFactory(cellData -> cellData.getValue().userProperty());

        // Настройка выбора строк для редактирования и удаления
        transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    // Загрузка категорий в ComboBox
    private void loadCategories(CategoryDAO dao, ComboBox<String> comboBox) {
        List<GeneralCategory> categories;
        try {
            categories = dao.getAll();
            comboBox.getItems().add("Все");
            for (GeneralCategory category : categories) {
                comboBox.getItems().add(category.getName());
            }
        } catch (SQLException e) {
            LoginApp.showError("Не удалось получить катеогрии.");
        }
        comboBox.setValue("Все");
    }

    // Загрузка пользователей
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

    // Обработчики кнопок для добавления и просмотра
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

    // Обновление данных в таблице
    @FXML
    private void handleRefresh() {
        refreshData();
    }

    private void refreshData() {
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

        // Обновляем таблицу
        transactionTable.getItems().setAll(transactions);

        // Подсчитываем доходы и расходы
        for (TransactionRecord transaction : transactions) {
            totalIncome = totalIncome.add(Objects.equals(transaction.getType(), "income") ? transaction.getAmount() : BigDecimal.ZERO);
            totalExpense = totalExpense.add(Objects.equals(transaction.getType(), "expense") ? transaction.getAmount() : BigDecimal.ZERO);
        }

        totalIncomeLabel.setText(String.format("%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("%.2f", totalExpense));
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
