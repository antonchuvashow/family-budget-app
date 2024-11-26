package antonchuvashov.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Income implements TransactionRecord{
    private final int incomeId;
    private final SimpleStringProperty userId;
    private final SimpleObjectProperty<BigDecimal> amount;
    private final ObservableValue<LocalDate> operationDate;
    private final int categoryId;
    private final SimpleStringProperty categoryName;

    public Income(int incomeId, String userId, BigDecimal amount, LocalDate operationDate, int categoryId, String categoryName) {
        this.incomeId = incomeId;
        this.userId = new SimpleStringProperty(userId);
        this.amount = new SimpleObjectProperty<>(amount);
        this.categoryId = categoryId;
        this.operationDate = new SimpleObjectProperty<>(operationDate);
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
        return this.operationDate;
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
    public String getType() {
        return "income";
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
        return operationDate.getValue();
    }

    @Override
    public String getColor() {
        return "green";
    }

    public int getCategoryId() {
        return categoryId;
    }
}
