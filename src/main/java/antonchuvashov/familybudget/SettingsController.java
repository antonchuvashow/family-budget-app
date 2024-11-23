package antonchuvashov.familybudget;

import antonchuvashov.daopost.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private void handleSaveSettings() {
        String url = dbUrlField.getText();
        String username = dbUsernameField.getText();
        String password = dbPasswordField.getText();

        if (url.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return;
        }

        DBConnection.setConnection(url, username, password);

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
