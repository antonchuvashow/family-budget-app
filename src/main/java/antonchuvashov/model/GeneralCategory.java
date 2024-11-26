package antonchuvashov.model;

import javafx.beans.property.StringProperty;

public interface GeneralCategory {

    public int getId();

    public void setId(int entryId);

    public String getName();

    public void setName(String name);

    public StringProperty nameProperty();
}
