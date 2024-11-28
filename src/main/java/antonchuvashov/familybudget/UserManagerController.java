package antonchuvashov.familybudget;

import antonchuvashov.daopost.UserDAO;
import antonchuvashov.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;

import static antonchuvashov.familybudget.LoginApp.showError;

public class UserManagerController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> fullNameColumn;

    @FXML
    private TableColumn<User, String> birthdayColumn;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Связываем колонки с данными
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        fullNameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        birthdayColumn.setCellValueFactory(cellData -> cellData.getValue().birthdayProperty());

        // Загружаем данные
        loadUsers();
    }

    private void loadUsers() {
        try {
            users.setAll(UserDAO.getAll());
            userTable.setItems(users);
        } catch (SQLException e) {
            showError("Не удалось загрузить пользователей.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleAddUser() {
        User newUser = showUserEditor(null);
        if (newUser != null) {
            try {
                UserDAO.add(newUser);
                loadUsers();
            } catch (SQLException e) {
                showError("Не удалось добавить пользователя.\n\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showError("Выберите пользователя для редактирования.");
            return;
        }

        User updatedUser = showUserEditor(selectedUser);
        if (updatedUser != null) {
            try {
                UserDAO.update(updatedUser);
                loadUsers();
            } catch (SQLException e) {
                showError("Не удалось обновить данные пользователя.\n\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showError("Выберите пользователя для удаления.");
            return;
        }

        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Вы уверены, что хотите удалить пользователя " + selectedUser.getUsername() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                UserDAO.delete(selectedUser.getUsername());
                loadUsers();
            } catch (SQLException e) {
                showError("Не удалось удалить пользователя.\n\n" + e.getMessage());
            }
        }
    }

    private User showUserEditor(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user_editor.fxml"));
            Stage stage = new Stage();
            stage.setTitle(user == null ? "Добавление пользователя" : "Редактирование пользователя");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            UserEditorController controller = loader.getController();
            controller.setContext(user);

            stage.showAndWait();

            return (User) stage.getUserData();
        } catch (Exception e) {
            showError("Не удалось открыть редактор пользователя.\n\n" + e.getMessage());
            return null;
        }
    }

}
