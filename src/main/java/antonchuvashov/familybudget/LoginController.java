package antonchuvashov.familybudget;

import antonchuvashov.daopost.DBConnection;
import antonchuvashov.daopost.UserDAO;
import antonchuvashov.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.SQLException;

import static antonchuvashov.daopost.UserDAO.isOlderThan;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Пожалуйста введите логин и пароль.");
            return;
        }

        try {
            // Проверка подключения к базе данных
            if (DBConnection.isValidConnection()) {
                // Логика успешного логина
                if (checkCredentials(username, password)) {
                    AuthenticationState.getInstance().login(username, isOlderThan(username, 18));
                    Stage loginStage = (Stage) usernameField.getScene().getWindow();

                    // Открытие главного окна
                    FXMLLoader loader = new FXMLLoader(LoginApp.class.getResource("main_window.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root, 400, 400));
                    stage.setMinWidth(800);
                    stage.setMinHeight(400);

                    stage.setTitle("Семейный бюджет");
                    stage.show();

                    loginStage.close();
                } else {
                    errorLabel.setText("Неверный логин или пароль.");
                }
            } else {
                errorLabel.setText("Не удалось подключиться к базе данных.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorLabel.setText("Произошла ошибка. Попробуйте еще раз.");
        }
    }

    @FXML
    private void openSettingsWindow() throws IOException {
        // Открытие окна настроек
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("settings_window.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, 300, 320));
        stage.setMinWidth(300);
        stage.setMaxWidth(300);
        stage.setMinHeight(320);
        stage.setMaxHeight(320);

        stage.setTitle("Настройки базы данных");
        stage.show();
    }

    private boolean checkCredentials(String username, String password) throws SQLException {
        User user = UserDAO.get(username);
        return user != null && user.getPassword().equals(password);
    }
}
