package antonchuvashov.familybudget;

import antonchuvashov.daopost.*;
import antonchuvashov.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static antonchuvashov.familybudget.MainApp.showError;

public class TransactionEditorController {

    @FXML
    public ComboBox<String> typeComboBox; // Для выбора типа транзакции (Доходы/Расходы)

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

    public void initialize() {
        // Инициализация ComboBox для выбора типа транзакции
        typeComboBox.getItems().addAll("Доходы", "Расходы");
        typeComboBox.setValue("Доходы"); // Устанавливаем "Доходы" по умолчанию

        // Обработчик изменения типа транзакции
        typeComboBox.setOnAction(event -> {
            loadCategories();
        });

        // Загрузка пользователей
        loadUsers();
    }

    public void setContext(TransactionRecord transaction) {
        this.transaction = transaction;

        if (transaction != null) {
            // Инициализация данных для редактирования
            typeComboBox.setValue(transaction instanceof Income ? "Доходы" : "Расходы");
            datePicker.setValue(transaction.getDate());
            categoryComboBox.setValue(transaction.getCategory().getName());
            userComboBox.setValue(transaction.getUser());
            amountField.setText(transaction.getAmount().toString());
        }

        loadCategories();
    }

    private void loadCategories() {
        try {
            List<GeneralCategory> categories;
            if ("Доходы".equals(typeComboBox.getValue())) {
                categories = IncomeCategoryDAO.getInstance().getAll();
            } else {
                categories = ExpenseCategoryDAO.getInstance().getAll();
            }

            categoryComboBox.getItems().clear();
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
            userComboBox.getItems().clear();
            userComboBox.setDisable(!AuthenticationState.getInstance().isAdmin());
            for (User user : users) {
                userComboBox.getItems().add(user.getUsername());
            }
            userComboBox.setValue(AuthenticationState.getInstance().getUsername());
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
            String type = typeComboBox.getValue();
            BigDecimal amount;
            LocalDate date = datePicker.getValue();

            if (category == null || user == null || type == null || date == null || amountField.getText().isEmpty()) {
                showError("Заполните все поля.");
                return;
            }

            try {
                amount = new BigDecimal(amountField.getText());
            } catch (NumberFormatException e) {
                showError("Некорректный формат суммы.");
                return;
            }

            boolean isIncome = "Доходы".equals(type);

            // Логика создания или обновления транзакции
            if (transaction == null) {
                // Новая транзакция
                if (isIncome) {
                    IncomeDAO.add(new Income(0, user, amount, date, IncomeCategoryDAO.getInstance().get(category)));
                } else {
                    ExpenseDAO.add(new Expense(0, user, amount, date, ExpenseCategoryDAO.getInstance().get(category)));
                }
            } else {
                // Обновление существующей транзакции
                boolean isTransactionIncome = transaction instanceof Income;

                // Обновляем общие поля
                transaction.setAmount(amount);
                transaction.setUser(user);
                transaction.setDate(date);
                transaction.setCategory(isIncome
                        ? IncomeCategoryDAO.getInstance().get(category)
                        : ExpenseCategoryDAO.getInstance().get(category));

                if (isTransactionIncome == isIncome) {
                    // Тип транзакции не изменился
                    if (isIncome) {
                        IncomeDAO.update((Income) transaction);
                    } else {
                        ExpenseDAO.update((Expense) transaction);
                    }
                } else {
                    // Тип транзакции изменился
                    if (isTransactionIncome) {
                        // Удаляем доход, создаём расход
                        IncomeDAO.delete(transaction.getId());
                        ExpenseDAO.add(new Expense(0, user, amount, date, ExpenseCategoryDAO.getInstance().get(category)));
                    } else {
                        // Удаляем расход, создаём доход
                        ExpenseDAO.delete(transaction.getId());
                        IncomeDAO.add(new Income(0, user, amount, date, IncomeCategoryDAO.getInstance().get(category)));
                    }
                }
            }

            close();
        } catch (SQLException e) {
            e.printStackTrace();
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
