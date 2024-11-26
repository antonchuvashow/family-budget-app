package antonchuvashov.model;

import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public interface TransactionRecord {
    public int getId();

    public BigDecimal getAmount();

    public String getName();

    public LocalDate getDate();

    public String getColor();

    public String getUser();

    ObservableValue<LocalDate> dateProperty();

    ObservableValue<String> categoryProperty();

    ObservableValue<BigDecimal> amountProperty();

    ObservableValue<String> userProperty();

    void setAmount(BigDecimal amount);

    void setUser(String user);

    void setDate(LocalDate date);
}
