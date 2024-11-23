package antonchuvashov.model;

import java.math.BigDecimal;
import java.util.Date;

public class Expense {
    private int expenseId;
    private String userId;
    private BigDecimal amount;
    private Date operationDate;
    private int entryId;

    public Expense(int expenseId, String userId, BigDecimal amount, Date operationDate, int entryId) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.amount = amount;
        this.operationDate = operationDate;
        this.entryId = entryId;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getOperationDate() {
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
