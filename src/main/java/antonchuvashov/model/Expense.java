package antonchuvashov.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class Expense implements TransactionRecord {
    private final int expenseId;
    private final SimpleStringProperty userId;
    private final SimpleObjectProperty<BigDecimal> amount;
    private SimpleObjectProperty<LocalDate> date;
    private final int entryId;
    private final SimpleStringProperty entryName;

    public Expense(int expenseId, String userId, BigDecimal amount, LocalDate date, int entryId, String entryName) {
        this.expenseId = expenseId;
        this.userId = new SimpleStringProperty(userId);
        this.amount = new SimpleObjectProperty<>(amount.negate());
        this.date = new SimpleObjectProperty<>(date);
        this.entryId = entryId;
        this.entryName = new SimpleStringProperty(entryName);
    }

    public int getExpenseId() {
        return expenseId;
    }

    @Override
    public String getUser() {
        return userId.getValue();
    }

    @Override
    public ObservableValue<LocalDate> dateProperty() {
        return this.date;
    }

    @Override
    public ObservableValue<String> categoryProperty() {
        return this.entryName;
    }

    @Override
    public ObservableValue<BigDecimal> amountProperty() {
        return this.amount;
    }

    @Override
    public ObservableValue<String> userProperty() {
        return this.userId;
    }

    @Override
    public void setAmount(BigDecimal amount) {
        this.amount.set(amount);
    }

    @Override
    public void setUser(String user) {
        this.userId.set(user);
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = new SimpleObjectProperty<>(date);
    }

    @Override
    public int getId() {
        return this.expenseId;
    }

    @Override
    public BigDecimal getAmount() {
        return amount.getValue();
    }

    @Override
    public String getName() {
        return entryName.getValue();
    }

    @Override
    public String getColor() {
        return "crimson";
    }

    @Override
    public LocalDate getDate() {
        return date.getValue();
    }

    public int getEntryId() {
        return entryId;
    }
}
