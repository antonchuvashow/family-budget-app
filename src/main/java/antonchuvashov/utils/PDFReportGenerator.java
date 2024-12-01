package antonchuvashov.utils;

import antonchuvashov.familybudget.MainApp;
import antonchuvashov.model.TransactionRecord;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

public class PDFReportGenerator {

    public static void generateReportWithChart(List<TransactionRecord> transactions, BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal averageIncome, BigDecimal averageExpense) throws IOException {
        PdfFont font = PdfFontFactory.createFont(Objects.requireNonNull(MainApp.class.getResource("times_new_roman.ttf")).getPath(), PdfEncodings.IDENTITY_H);

        // Создаем документ PDF
        File file = new File("./financial_report.pdf");
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Применяем шрифт к документу
        document.setFont(font);

        document.add(new Paragraph("Отчет по доходам и расходам")
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Дата: " + LocalDate.now())
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT));

        // Таблица с данными
        Table table = new Table(3);

        table.addCell(new Cell().add(new Paragraph("Дата")));
        table.addCell(new Cell().add(new Paragraph("Категория")));
        table.addCell(new Cell().add(new Paragraph("Сумма")));

        for (TransactionRecord transaction : transactions) {
            table.addCell(transaction.getDate().toString());
            table.addCell(transaction.getCategory().getName());
            table.addCell(transaction.getSignedAmount());
        }

        document.add(table);

        // Добавляем итоговые данные
        document.add(new Paragraph("Итоговые данные:")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.LEFT));

        document.add(new Paragraph("Общий доход: " + totalIncome).setFontSize(12));
        document.add(new Paragraph("Общий расход: " + totalExpense).setFontSize(12));
        document.add(new Paragraph("Средний ежедневный доход: " + averageIncome).setFontSize(12));
        document.add(new Paragraph("Средний ежедневный расход: " + averageExpense).setFontSize(12));

        // Генерация графика и вставка в документ
        BufferedImage chartImage = ChartGenerator.generateChart(transactions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        Image img = new Image(ImageDataFactory.create(baos.toByteArray()));
        img.setWidth(500);
        document.add(img);

        document.close();
    }
}
