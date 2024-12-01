package antonchuvashov.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IncomeCategory implements GeneralCategory {
    private int id;
    private final StringProperty name;

    public IncomeCategory(int id, String name) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int entryId) {
        this.id = entryId;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }
}
