package antonchuvashov.utils;

import antonchuvashov.model.Expense;
import antonchuvashov.model.Income;
import antonchuvashov.model.TransactionRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List;

public class ChartGenerator {

    public static BufferedImage generateChart(List<TransactionRecord> transactions) {
        DefaultCategoryDataset dataset = getDefaultCategoryDataset(transactions);

        // Создаем график
        JFreeChart chart = ChartFactory.createBarChart(
                "Доходы и Расходы по категориям",   // Заголовок графика
                "Категория",                       // Ось X
                "Сумма",                           // Ось Y
                dataset,                           // Набор данных
                PlotOrientation.VERTICAL,          // Ориентация
                true,                              // Легенда
                true,                              // Подсказки
                false                              // URL
        );

        // Настройка фона и цвета
        chart.setBackgroundPaint(Color.white); // Белый фон графика
        chart.getLegend().setItemFont(new Font("Times New Roman", Font.PLAIN, 28));
        chart.getTitle().setFont(new Font("Times New Roman", Font.PLAIN, 32));
        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(new Color(240, 240, 240)); // Светлый серый фон для оси
        plot.setDomainGridlinePaint(Color.white); // Линии сетки по оси X белые
        plot.setRangeGridlinePaint(new Color(211, 211, 211)); // Линии сетки по оси Y светло-серые

        // Настройка шрифтов
        plot.getDomainAxis().setLabelFont(new Font("Times New Roman", Font.PLAIN, 32));
        plot.getDomainAxis().setTickLabelFont(new Font("Times New Roman", Font.PLAIN, 28));
        plot.getRangeAxis().setLabelFont(new Font("Times New Roman", Font.PLAIN, 32));
        plot.getRangeAxis().setTickLabelFont(new Font("Times New Roman", Font.PLAIN, 32));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(34, 139, 34)); // Доходы — зеленый
        renderer.setSeriesPaint(1, new Color(255, 69, 0));  // Расходы — оранжевый

        // Установим отступы графика и сетки
        plot.setInsets(new RectangleInsets(10, 10, 10, 10));

        // Генерация изображения графика
        return chart.createBufferedImage(1600, 1200);
    }

    private static DefaultCategoryDataset getDefaultCategoryDataset(List<TransactionRecord> transactions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Группируем по категориям
        for (TransactionRecord transaction : transactions) {
            String category = transaction.getCategory().getName();
            BigDecimal amount = transaction.getAmount();

            if (Income.class.equals(transaction.getClass())) {
                dataset.addValue(amount.doubleValue(), "Доходы", category);
            } else if (Expense.class.equals(transaction.getClass())) {
                dataset.addValue(amount.doubleValue(), "Расходы", category);
            }
        }
        return dataset;
    }
}
