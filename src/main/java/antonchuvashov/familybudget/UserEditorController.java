package antonchuvashov.familybudget;

import antonchuvashov.model.User;
import antonchuvashov.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import static antonchuvashov.familybudget.LoginApp.showError;

public class UserEditorController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField fullNameField;

    @FXML
    private DatePicker birthdayPicker;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private User user; // текущий редактируемый пользователь

    private boolean isEditMode = false; // флаг редактирования

    public void setContext(User user) {
        this.user = user;

        if (user != null) {
            // Инициализация полей для редактирования
            isEditMode = true;
            titleLabel.setText("Редактирование пользователя");
            usernameField.setText(user.getUsername());
            usernameField.setDisable(true); // Имя пользователя нельзя изменить
            fullNameField.setText(user.getFullName());
            birthdayPicker.setValue(LocalDate.ofInstant(user.getBirthday().toInstant(), ZoneId.systemDefault()));
        } else {
            // Инициализация для добавления
            isEditMode = false;
            titleLabel.setText("Добавление пользователя");
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Проверка и извлечение данных
            String username = usernameField.getText();
            String passwordHash = PasswordUtils.hashPassword(passwordField.getText());
            String fullName = fullNameField.getText();
            LocalDate birthday = birthdayPicker.getValue();

            if (username.isEmpty() || passwordHash.isEmpty() || fullName.isEmpty() || birthday == null) {
                showError("Все поля должны быть заполнены.");
                return;
            }

            // Создаём/обновляем объект пользователя
            if (isEditMode) {
                user.setPasswordHash(passwordHash);
                user.setFullName(fullName);
                user.setBirthday(Date.valueOf(birthday));
            } else {
                user = new User(username, passwordHash, fullName, Date.valueOf(birthday));
            }

            // Закрываем окно, возвращаем пользователя
            close(true);
        } catch (Exception e) {
            showError("Ошибка при сохранении: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        close(false);
    }

    private void close(boolean isSaved) {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        if (isSaved) {
            stage.setUserData(user); // Передаём пользователя в окно-родитель
        } else {
            stage.setUserData(null); // Передаём null, если отмена
        }
        stage.close();
    }
}
