package antonchuvashov.utils;

import antonchuvashov.model.Expense;
import antonchuvashov.model.Income;
import antonchuvashov.model.TransactionRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Statistics {
    public static StatsResult getStats(List<TransactionRecord> transactions, long daysInPeriod) {
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (TransactionRecord transaction : transactions) {
            totalIncome = totalIncome.add(Income.class.equals(transaction.getClass()) ?
                    transaction.getAmount() : BigDecimal.ZERO);
            totalExpense = totalExpense.add(Expense.class.equals(transaction.getClass()) ?
                    transaction.getAmount() : BigDecimal.ZERO);
        }

        BigDecimal averageIncome = daysInPeriod > 0 ? totalIncome.divide(BigDecimal.valueOf(daysInPeriod),
                2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal averageExpense = daysInPeriod > 0 ? totalExpense.divide(BigDecimal.valueOf(daysInPeriod),
                2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        return new StatsResult(totalIncome, totalExpense, averageIncome, averageExpense);
    }

    public record StatsResult(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal averageIncome,
                              BigDecimal averageExpense) {
    }
}
