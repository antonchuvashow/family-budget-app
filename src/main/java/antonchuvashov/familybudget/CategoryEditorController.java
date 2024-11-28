package antonchuvashov.familybudget;

import antonchuvashov.daopost.CategoryDAO;
import antonchuvashov.model.GeneralCategory;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static antonchuvashov.familybudget.LoginApp.showError;

public class CategoryEditorController {
    @FXML
    public Button deleteHandler;
    @FXML
    public Button editHandler;
    @FXML
    private Label titleLabel;

    @FXML
    private TableView<GeneralCategory> generalCategoryTableView;

    @FXML
    private TableColumn<GeneralCategory, String> nameColumn;

    private CategoryDAO dao;

    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        deleteHandler.setDisable(!AuthenticationState.getInstance().isAdmin());
        editHandler.setDisable(!AuthenticationState.getInstance().isAdmin());
    }

    public void setContext(String title, CategoryDAO dao) {
        titleLabel.setText(title);
        this.dao = dao;
        loadEntries();
    }

    private void loadEntries() {
        try {
            List<GeneralCategory> entries = dao.getAll();
            generalCategoryTableView.getItems().setAll(entries);
        } catch (SQLException e) {
            showError("Не удалось загрузить данные.");
        }
    }

    @FXML
    private void handleAdd() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавить статью");
        dialog.setHeaderText(null);
        dialog.setContentText("Введите название:");

        dialog.showAndWait().ifPresent(name -> {
            try {
                dao.add(name);
                loadEntries();
            } catch (SQLException e) {
                showError("Не удалось добавить статью.");
            }
        });
    }

    @FXML
    private void handleEdit() {
        GeneralCategory category = generalCategoryTableView.getSelectionModel().getSelectedItem();
        if (category == null) {
            showError("Выберите статью для изменения.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(category.getName());
        dialog.setTitle("Изменить статью");
        dialog.setHeaderText(null);
        dialog.setContentText("Введите новое название:");

        dialog.showAndWait().ifPresent(newName -> {
            try {
                dao.update(category.getId(), newName);
                loadEntries();
            } catch (SQLException e) {
                showError("Не удалось изменить статью.");
            }
        });
    }

    @FXML
    private void handleDelete() {
        GeneralCategory category = generalCategoryTableView.getSelectionModel().getSelectedItem();
        if (category == null) {
            showError("Выберите статью для удаления.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Вы уверены, что хотите удалить " + category.getName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                dao.delete(category.getId());
                loadEntries();
            } catch (SQLException e) {
                showError("Не удалось удалить статью.\n\n" + e.getMessage());
            }
        }
    }
}
