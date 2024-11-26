package antonchuvashov.model;

import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public interface TransactionRecord {
    public String getType();

    public BigDecimal getAmount();

    public String getName();

    public LocalDate getDate();

    public String getColor();

    public String getUser();

    ObservableValue<LocalDate> dateProperty();

    ObservableValue<String> categoryProperty();

    ObservableValue<BigDecimal> amountProperty();

    ObservableValue<String> userProperty();
}
