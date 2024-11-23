package antonchuvashov.model;

import java.math.BigDecimal;
import java.util.Date;

public class Income {
    private int incomeId;
    private String userId;
    private BigDecimal amount;
    private Date operationDate;
    private int categoryId;

    public Income(int incomeId, String userId, BigDecimal amount, Date operationDate, int categoryId) {
        this.incomeId = incomeId;
        this.userId = userId;
        this.amount = amount;
        this.categoryId = categoryId;
        this.operationDate = operationDate;
    }

    public int getIncomeId() {
        return incomeId;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
