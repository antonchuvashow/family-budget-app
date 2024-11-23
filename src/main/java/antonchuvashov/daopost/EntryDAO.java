package antonchuvashov.daopost;

import antonchuvashov.model.Entry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntryDAO {

    public static void addEntry(Entry entry) throws SQLException {
        String query = "INSERT INTO ENTRY (entry_id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, entry.getEntryId());
            preparedStatement.setString(2, entry.getName());
            preparedStatement.executeUpdate();
        }
    }

    public static Entry getEntryById(int entryId) throws SQLException {
        String query = "SELECT * FROM ENTRY WHERE entry_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, entryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Entry(
                        resultSet.getInt("entry_id"),
                        resultSet.getString("name")
                );
            }
        }
        return null;
    }

    public static List<Entry> getAllEntries() throws SQLException {
        String query = "SELECT * FROM ENTRY";
        List<Entry> entries = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                entries.add(new Entry(
                        resultSet.getInt("entry_id"),
                        resultSet.getString("name")
                ));
            }
        }
        return entries;
    }

    public static void updateEntry(Entry entry) throws SQLException {
        String query = "UPDATE ENTRY SET name = ? WHERE entry_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, entry.getName());
            preparedStatement.setInt(2, entry.getEntryId());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteEntry(int entryId) throws SQLException {
        String query = "DELETE FROM ENTRY WHERE entry_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, entryId);
            preparedStatement.executeUpdate();
        }
    }
}
