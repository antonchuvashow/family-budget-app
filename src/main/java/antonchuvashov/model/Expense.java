package antonchuvashov.model;

import java.math.BigDecimal;
import java.util.Date;

public class Expense implements TransactionRecord {
    private int expenseId;
    private String userId;
    private BigDecimal amount;
    private Date operationDate;
    private int entryId;
    private final String entryName;

    public Expense(int expenseId, String userId, BigDecimal amount, Date operationDate, int entryId, String entryName) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.amount = amount;
        this.operationDate = operationDate;
        this.entryId = entryId;
        this.entryName = entryName;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public String getUser() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getType() {
        return "expense";
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String getName() {
        return entryName;
    }

    @Override
    public String getColor() {
        return "crimson";
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Date getDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
}
