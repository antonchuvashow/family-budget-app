package antonchuvashov.model;

import java.math.BigDecimal;
import java.util.Date;

public class Income implements TransactionRecord{
    private final int incomeId;
    private final String userId;
    private final BigDecimal amount;
    private final Date operationDate;
    private final int categoryId;
    private final String categoryName;

    public Income(int incomeId, String userId, BigDecimal amount, Date operationDate, int categoryId, String categoryName) {
        this.incomeId = incomeId;
        this.userId = userId;
        this.amount = amount;
        this.categoryId = categoryId;
        this.operationDate = operationDate;
        this.categoryName = categoryName;
    }

    public int getIncomeId() {
        return incomeId;
    }

    @Override
    public String getUser() {
        return userId;
    }

    @Override
    public String getType() {
        return "income";
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String getName() {
        return categoryName;
    }

    public Date getDate() {
        return operationDate;
    }

    @Override
    public String getColor() {
        return "green";
    }

    public int getCategoryId() {
        return categoryId;
    }
}
