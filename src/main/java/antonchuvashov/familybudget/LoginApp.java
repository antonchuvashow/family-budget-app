package antonchuvashov.familybudget;

import antonchuvashov.daopost.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        DBConnection.setConnection("localhost:5432/postgres", "user", "password");
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
