package antonchuvashov.familybudget;

import antonchuvashov.daopost.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApp extends Application {

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) throws IOException {
        DBConnection.fromSettings();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("login_window.fxml"));
        stage.setTitle("Вход");
        stage.setScene(new Scene(fxmlLoader.load(), 300, 350));
        stage.setMinWidth(300);
        stage.setMaxWidth(300);
        stage.setMinHeight(380);
        stage.setMaxHeight(380);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}