package antonchuvashov.model;

import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public interface TransactionRecord {
    public int getId();

    public BigDecimal getAmount();

    public GeneralCategory getCategory();

    public LocalDate getDate();

    public String getUser();

    ObservableValue<LocalDate> dateProperty();

    ObservableValue<BigDecimal> amountProperty();

    ObservableValue<String> userProperty();

    void setAmount(BigDecimal amount);

    void setUser(String user);

    void setDate(LocalDate date);

    void setCategory(GeneralCategory category);

    public String getSignedAmount();
}
