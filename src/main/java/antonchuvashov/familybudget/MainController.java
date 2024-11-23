package antonchuvashov.familybudget;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainController {

    @FXML
    private void handleViewBudget() {
        System.out.println("Viewing budget...");
    }

    @FXML
    private void handleExit() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("login-form.fxml"));
        Stage stage = fxmlLoader.<Stage>getController();
        stage.close();
    }
}
