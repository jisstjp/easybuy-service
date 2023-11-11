package com.stock.inventorymanagement.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stock.inventorymanagement.dto.CartItemDto;
import com.stock.inventorymanagement.service.IPdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PdfGenerationServiceImpl implements IPdfGenerationService {

    @Autowired
    private CartItemServiceImpl cartItemService;
    @Autowired
    ProductServiceImpl productService;

    @Override
    public byte[] generateOrderSummaryPdf(Long cartId) {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Styling Fonts and Colors
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Company Name and Date Header
            document.add(new Paragraph("maclom.com", titleFont));
            document.add(new Paragraph("Date: " + dateFormat.format(new Date()), textFont));
            document.add(Chunk.NEWLINE);

            // Fetching Cart Items and Calculating Prices
            List<CartItemDto> cartItems = cartItemService.getCartItemsByCartId(cartId);
            PdfPTable table = new PdfPTable(new float[]{3, 1, 2, 2});
            table.setWidthPercentage(100);

            // Adding table headers
            Stream.of("Product Name", "Quantity", "Price per Item", "Subtotal")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.BLUE);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, headerFont));
                        table.addCell(header);
                    });

            BigDecimal total = BigDecimal.ZERO;

            // Adding cart items
            for (CartItemDto item : cartItems) {
                String productName = productService.getProductNameById(item.getProductId());
                /*
                BigDecimal lineTotal = productService.getSalesPrice(item.getProductId())
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                 */

                BigDecimal price = safelyFetchSalesPrice(item.getProductId(), item.getPrice());

                BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

                table.addCell(new Phrase(productName, textFont));
                table.addCell(new Phrase(String.valueOf(item.getQuantity()), textFont));
                table.addCell(new Phrase("$" + price.toPlainString(), textFont));
                table.addCell(new Phrase("$" + lineTotal.toPlainString(), textFont));

                total = total.add(lineTotal);
            }

            document.add(table);

            // Total Cost
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Total Cost: $" + total.toPlainString(), titleFont));

            // Footer - Privacy Notice and Rights Reserved
            document.add(Chunk.NEWLINE);
            Paragraph privacyNotice = new Paragraph("Privacy Notice: All personal information is handled in accordance with our privacy policy.", textFont);
            document.add(privacyNotice);

            Paragraph rightsReserved = new Paragraph("All rights reserved © maclom.com", textFont);
            rightsReserved.setAlignment(Element.ALIGN_RIGHT);
            document.add(rightsReserved);

        } catch (DocumentException e) {
            throw new RuntimeException("Error while generating PDF", e);
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }

    private BigDecimal safelyFetchSalesPrice(Long productId, BigDecimal fallbackPrice) {
        try {
            return productService.getSalesPrice(productId);
        } catch (Exception e) {
            // Log the exception if necessary
            return fallbackPrice;
        }
    }
}
