package com.stock.inventorymanagement.service.impl;
/*
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stock.inventorymanagement.dto.CartDto;
import com.stock.inventorymanagement.dto.CartItemDto;
import com.stock.inventorymanagement.dto.CustomerDto;
import com.stock.inventorymanagement.service.IPdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfGenerationServiceImpl implements IPdfGenerationService {

    @Autowired
    private CartItemServiceImpl cartItemService;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    CartServiceImpl cartService;
    @Autowired
    CustomerServiceImpl CustomerService;

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

            CartDto cartDto = cartService.getCartById(cartId);
            Long userId = cartDto.getUserId();

            CustomerDto customerDto = CustomerService.getCustomerById(userId);



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

/*

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
*/


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stock.inventorymanagement.dto.CartDto;
import com.stock.inventorymanagement.dto.CartItemDto;
import com.stock.inventorymanagement.dto.CustomerDto;
import com.stock.inventorymanagement.service.IPdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfGenerationServiceImpl implements IPdfGenerationService {

    @Autowired
    private CartItemServiceImpl cartItemService;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private CustomerServiceImpl customerService;

    @Override
    public byte[] generateOrderSummaryPdf(Long cartId) {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Header with Company Name and Date
            addHeader(document, "maclom.com", dateFormat.format(new Date()));

            // Fetching Cart and Customer Details
            CartDto cartDto = cartService.getCartById(cartId);
            Long userId = cartDto.getUserId();
            CustomerDto customerDto = customerService.getCustomerById(userId);

            // Beautified Customer Details
            addCustomerDetails(document, customerDto);

            // Adding Cart Items and Calculating Prices
            addCartItems(document, cartId);

            // Footer
            addFooter(document);

        } catch (DocumentException e) {
            throw new RuntimeException("Error while generating PDF", e);
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void addHeader(Document document, String companyName, String date) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        Paragraph companyNameParagraph = new Paragraph(companyName, titleFont);
        companyNameParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(companyNameParagraph);

        Paragraph dateParagraph = new Paragraph("Date: " + date, subTitleFont);
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(dateParagraph);
        document.add(Chunk.NEWLINE);
    }

    private void addCustomerDetails(Document document, CustomerDto customerDto) throws DocumentException {
        Font customerDetailsHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLUE);
        Font customerDetailsFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        Paragraph customerDetailsHeader = new Paragraph("Customer Information", customerDetailsHeaderFont);
        customerDetailsHeader.setAlignment(Element.ALIGN_LEFT);
        document.add(customerDetailsHeader);

        String customerInfo = String.format(
                "Name: %s %s\nEmail: %s\nPhone: %s\nAddress: %s, %s, %s, %s, %s",
                customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getEmail(), customerDto.getPhone(),
                customerDto.getAddressLine1(), customerDto.getAddressLine2(),
                customerDto.getCity(), customerDto.getStateProvince(),
                customerDto.getCountry()
        );
        Paragraph customerInfoParagraph = new Paragraph(customerInfo, customerDetailsFont);
        customerInfoParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(customerInfoParagraph);
        document.add(Chunk.NEWLINE);
    }

    private void addCartItems(Document document, Long cartId) throws DocumentException {
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
                    header.setPhrase(new Phrase(columnTitle, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
                    table.addCell(header);
                });

        BigDecimal total = BigDecimal.ZERO;

        // Adding cart items
        for (CartItemDto item : cartItems) {
            String productName = productService.getProductNameById(item.getProductId());
            BigDecimal price = safelyFetchSalesPrice(item.getProductId(), item.getPrice());
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            table.addCell(new Phrase(productName, new Font(Font.FontFamily.HELVETICA, 12)));
            table.addCell(new Phrase(String.valueOf(item.getQuantity()), new Font(Font.FontFamily.HELVETICA, 12)));
            table.addCell(new Phrase("$" + price.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12)));
            table.addCell(new Phrase("$" + lineTotal.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12)));

            total = total.add(lineTotal);
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Total Cost: $" + total.toPlainString(), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
        document.add(Chunk.NEWLINE);
    }

    private void addFooter(Document document) throws DocumentException {
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Paragraph footerParagraph = new Paragraph("All rights reserved © maclom.com", footerFont);
        footerParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(footerParagraph);
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
