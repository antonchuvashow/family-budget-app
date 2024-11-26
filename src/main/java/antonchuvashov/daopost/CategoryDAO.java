package antonchuvashov.daopost;

import antonchuvashov.model.GeneralCategory;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {

    public static CategoryDAO getInstance() {
        return null;
    }

    public GeneralCategory get(int id) throws SQLException;

    public List<GeneralCategory> getAll() throws SQLException;

    public void add(GeneralCategory generalCategory) throws SQLException;

    public void add(String name) throws SQLException;

    public void update(int id, String newName) throws SQLException;

    public void delete(int id) throws SQLException;
}
