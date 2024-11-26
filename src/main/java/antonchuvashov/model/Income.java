package antonchuvashov.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income implements TransactionRecord {
    private final int incomeId;
    private final SimpleStringProperty userId;
    private final SimpleObjectProperty<BigDecimal> amount;
    private ObservableValue<LocalDate> date;
    private final int categoryId;
    private final SimpleStringProperty categoryName;

    public Income(int incomeId, String userId, BigDecimal amount, LocalDate date, int categoryId, String categoryName) {
        this.incomeId = incomeId;
        this.userId = new SimpleStringProperty(userId);
        this.amount = new SimpleObjectProperty<>(amount);
        this.categoryId = categoryId;
        this.date = new SimpleObjectProperty<>(date);
        this.categoryName = new SimpleStringProperty(categoryName);
    }

    public int getIncomeId() {
        return incomeId;
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
        return this.categoryName;
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
        return this.incomeId;
    }

    @Override
    public BigDecimal getAmount() {
        return amount.getValue();
    }

    @Override
    public String getName() {
        return categoryName.getValue();
    }

    public LocalDate getDate() {
        return date.getValue();
    }

    @Override
    public String getColor() {
        return "green";
    }

    public int getCategoryId() {
        return categoryId;
    }
}
