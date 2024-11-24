package antonchuvashov.model;

import java.math.BigDecimal;
import java.util.Date;

public interface TransactionRecord {
    public String getType();

    public BigDecimal getAmount();

    public String getName();

    public Date getDate();

    public String getColor();

    public String getUser();
}
