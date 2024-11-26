package antonchuvashov.familybudget;

import antonchuvashov.daopost.*;
import antonchuvashov.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static antonchuvashov.familybudget.LoginApp.showError;

public class TransactionEditorController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private TransactionRecord transaction; // текущая редактируемая транзакция
    private boolean isIncome; // флаг для типа транзакции

    public void initialize() {
        // Настройка интерфейса
        categoryComboBox.getItems().add("Выберите категорию");
        userComboBox.getItems().add("Выберите пользователя");
        amountField.setPromptText("Введите сумму");
    }

    public void setContext(TransactionRecord transaction) {
        this.transaction = transaction;

        if (transaction != null) {
            // Инициализация данных для редактирования
            this.isIncome = transaction.getClass().equals(Income.class);
            datePicker.setValue(transaction.getDate());
            categoryComboBox.setValue(transaction.getName());
            userComboBox.setValue(transaction.getUser());
            amountField.setText(transaction.getAmount().toString());
        }

        // Загрузка категорий и пользователей
        loadCategories();
        loadUsers();
    }

    private void loadCategories() {
        try {
            List<GeneralCategory> categories = isIncome
                    ? IncomeCategoryDAO.getInstance().getAll()
                    : ExpenseCategoryDAO.getInstance().getAll();

            for (GeneralCategory category : categories) {
                categoryComboBox.getItems().add(category.getName());
            }
        } catch (SQLException e) {
            showError("Не удалось загрузить категории.\n\n" + e.getMessage());
        }
    }

    private void loadUsers() {
        try {
            List<User> users = UserDAO.getAll();
            for (User user : users) {
                userComboBox.getItems().add(user.getUsername());
            }
        } catch (SQLException e) {
            showError("Не удалось загрузить пользователей.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Проверка и извлечение данных
            String category = categoryComboBox.getValue();
            String user = userComboBox.getValue();
            BigDecimal amount = new BigDecimal(amountField.getText());
            LocalDate date = datePicker.getValue();

            if (category == null || user == null || amount == null || date == null) {
                showError("Заполните все поля.");
                return;
            }
            isIncome = amount.compareTo(BigDecimal.ZERO) > 0;

            if (transaction == null) {
                // Создание новой транзакции
                // TODO: Переделать классы Income и Expense, сделать нормально, а не вот это вот...
                if (isIncome) {
                    IncomeDAO.add(new Income(0, user, amount, date, 0, category));
                } else {
                    ExpenseDAO.add(new Expense(0, user, amount, date, 0, category));
                }
            } else {
                // Обновление существующей транзакции
                transaction.setAmount(amount);
                transaction.setUser(user);
                transaction.setDate(date);
                if (isIncome) {
                    IncomeDAO.update((Income) transaction);
                } else {
                    ExpenseDAO.update((Expense) transaction);
                }
            }

            // Закрываем окно
            close();
        } catch (SQLException e) {
            showError("Не удалось сохранить изменения.\n\n" + e.getMessage());
        } catch (NumberFormatException e) {
            showError("Некорректный формат суммы.");
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
