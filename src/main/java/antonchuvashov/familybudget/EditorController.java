package antonchuvashov.familybudget;

import antonchuvashov.daopost.CategoryDAO;
import antonchuvashov.model.GeneralCategory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

import static antonchuvashov.familybudget.LoginApp.showError;

public class EditorController {

    @FXML
    private Label titleLabel;

    @FXML
    private TableView<GeneralCategory> entryTable;

    @FXML
    private TableColumn<GeneralCategory, String> nameColumn;

    private CategoryDAO dao;

    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    public void setContext(String title, CategoryDAO dao) {
        titleLabel.setText(title);
        this.dao = dao;
        loadEntries();
    }

    private void loadEntries() {
        try {
            List<GeneralCategory> entries = dao.getAll();
            entryTable.getItems().setAll(entries);
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
        GeneralCategory category = entryTable.getSelectionModel().getSelectedItem();
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
        GeneralCategory selectedExpenseCategory = entryTable.getSelectionModel().getSelectedItem();
        if (selectedExpenseCategory == null) {
            showError("Выберите статью для удаления.");
            return;
        }

        try {
            dao.delete(selectedExpenseCategory.getId());
            loadEntries();
        } catch (SQLException e) {
            showError("Не удалось удалить статью.");
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) entryTable.getScene().getWindow();
        stage.close();
    }
}
