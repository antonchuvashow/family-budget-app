package antonchuvashov.familybudget;

import antonchuvashov.daopost.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class SettingsController {

    @FXML
    private TextField dbUrlField;

    @FXML
    private TextField dbUsernameField;

    @FXML
    private PasswordField dbPasswordField;

    public void initialize() {
        dbUrlField.setText(DBConnection.getDbUrl());
        dbUsernameField.setText(DBConnection.getDbUsername());
        dbPasswordField.setText(DBConnection.getDbPassword());
    }

    @FXML
    private void handleSaveSettings() throws IOException {
        String url = dbUrlField.getText();
        String username = dbUsernameField.getText();
        String password = dbPasswordField.getText();

        if (url.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return;
        }

        Properties properties = new Properties();
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("settings")) {
            properties.load(inputStream);

            properties.setProperty("db.url", url);
            properties.setProperty("db.user", username);
            properties.setProperty("db.password", password);

            try (PrintWriter writer = new PrintWriter(
                            Objects.requireNonNull(DBConnection.class.getClassLoader().getResource("settings")).getPath())) {
                properties.store(writer, null);
            }

            DBConnection.setConnection(url, username, password);
        }

        Stage settingsStage = (Stage) dbUrlField.getScene().getWindow();
        settingsStage.close();
    }

    @FXML
    private void handleCancel() {
        // Закрытие окна настроек без сохранения
        Stage settingsStage = (Stage) dbUrlField.getScene().getWindow();
        settingsStage.close();
    }
}
