package com.example.email_service.services.implementations;

import com.example.email_service.dtos.OrderItemDtoRequest;
import com.example.email_service.exceptions.PdfGenerationException;
import com.example.email_service.services.PdfGeneratorService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private static final Logger logger = Logger.getLogger(PdfGeneratorServiceImpl.class.getName());

    @Override
    public byte[] generateOrderPdf(String email, List<OrderItemDtoRequest> orderItems) throws java.io.IOException {

        try {


            logger.info("Starting PDF generation order for user with email: " + email);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

            Paragraph title = new Paragraph("Your Order Details #")
                    .setFont(boldFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 2, 1, 1, 1}))
                    .useAllAvailableWidth();


            table.addHeaderCell(new Cell().add(new Paragraph("Product ID").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setFont(boldFont)));


            double total = 0.0;
            for (OrderItemDtoRequest item : orderItems) {
//            double subtotal = item.getQuantity() * item.getPrice();
//            total += subtotal;


                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.productId())).setFont(normalFont)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.quantity())).setFont(normalFont)));

            }

            document.add(table);

            Paragraph totalText = new Paragraph("Total Amount: " + currencyFormat.format(total))
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(15);
            document.add(totalText);

            document.close();
            logger.info("PDF successfully generated");
            return outputStream.toByteArray();
        }catch (Exception e) {
            logger.log(Level.SEVERE, "ERROR generating PDF for user with email: " + email, e);
            throw new PdfGenerationException("Failed to generate PDF for user with email: " + email, e);
        }

    }

}
