package com.stock.inventorymanagement.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.stock.inventorymanagement.dto.*;
import com.stock.inventorymanagement.service.IPdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private OrderServiceImpl orderService; // Service to fetch order details

    @Override
    public byte[] generateOrderSummaryPreviewPdf(Long cartId) {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Header with Company Name and Date
            addHeader(document, "macolam.com", dateFormat.format(new Date()));

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
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);

        // Create a table for the header with two columns
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{3, 1});

        // Company name cell
        PdfPCell companyNameCell = new PdfPCell(new Phrase(companyName, titleFont));
        companyNameCell.setBorder(Rectangle.NO_BORDER);
        companyNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        companyNameCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        headerTable.addCell(companyNameCell);

        // Date cell
        PdfPCell dateCell = new PdfPCell(new Phrase("Date: " + date, subTitleFont));
        dateCell.setBorder(Rectangle.NO_BORDER);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dateCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        headerTable.addCell(dateCell);

        // Add the table to the document
        document.add(headerTable);
        document.add(Chunk.NEWLINE);  // Add a line break after the header
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


    @Override
    public byte[] generateOrderSummaryPdf(Long orderId) throws DocumentException {
        OrderDto orderDto = orderService.getOrder(orderId);
        CustomerDto customerDto = customerService.getCustomerByUserId(orderDto.getUserId());

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addCompanyHeader(document, "macolam.com");
            addOrderHeader(document, orderDto);
            addOrderCustomerDetails(document, customerDto, orderDto);
            addOrderDetails(document, orderDto);
            addPaymentDetails(document, orderDto);
            addOrderFooter(document);

            document.close();
            return outputStream.toByteArray();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void addCompanyHeader(Document document, String companyName) throws DocumentException {
        Paragraph companyHeader = new Paragraph(companyName, new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.DARK_GRAY));
        companyHeader.setAlignment(Element.ALIGN_CENTER);
        document.add(companyHeader);
        document.add(Chunk.NEWLINE);
    }

    private void addOrderHeader(Document document, OrderDto orderDto) throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = orderDto.getCreatedAt().format(formatter);

        Paragraph orderHeader = new Paragraph("Order Summary - #" + orderDto.getId(), new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        orderHeader.setAlignment(Element.ALIGN_CENTER);
        document.add(orderHeader);

        Paragraph date = new Paragraph("Date: " + formattedDate, new Font(Font.FontFamily.HELVETICA, 12));
        date.setAlignment(Element.ALIGN_CENTER);
        document.add(date);

        document.add(Chunk.NEWLINE);
    }

    private void addOrderCustomerDetails(Document document, CustomerDto customerDto, OrderDto orderDto) throws DocumentException {
        Paragraph title = new Paragraph("Customer Details", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
        title.setAlignment(Element.ALIGN_LEFT);
        document.add(title);

        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12);
        String customerInfo = String.format(
                "Name: %s %s\nEmail: %s\nPhone: %s\nShipping Address: %s",
                customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getEmail(), customerDto.getPhone(),
                orderDto.getShippingAddress()
        );

        Paragraph customerDetails = new Paragraph(customerInfo, infoFont);
        customerDetails.setSpacingBefore(5f);
        customerDetails.setSpacingAfter(5f);
        document.add(customerDetails);

        document.add(new LineSeparator(0.5f, 100, null, 0, -5));
        document.add(Chunk.NEWLINE);
    }

    private void addOrderDetails(Document document, OrderDto orderDto) throws DocumentException {
        // Adding a bit more space before the title
        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));

        Paragraph title = new Paragraph("Order Details", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingBefore(10f); // Adding space before the title
        document.add(title);

        // Creating the table for order details
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f); // Adding space before the table
        addTableHeader(table, new String[]{"Item", "Quantity", "Price"});

        // Populating the table with order item details
        for (OrderItemDto item : orderDto.getOrderItems()) {
            String productName = productService.getProductNameById(item.getProductId());
            table.addCell(new PdfPCell(new Phrase(productName, new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase("$" + item.getPrice().toPlainString(), new Font(Font.FontFamily.HELVETICA, 12))));
        }
        document.add(table);

        // Total Price
        Paragraph total = new Paragraph("Total Price: $" + orderDto.getTotalPrice().toPlainString(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(5f); // Adding space before the total price
        document.add(total);

        // Adding a bit more space after the section
        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));
    }


    private void addPaymentDetails(Document document, OrderDto orderDto) throws DocumentException {
        Paragraph title = new Paragraph("Payment Information", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        document.add(title);

        Paragraph paymentMethod = new Paragraph("Method: " + orderDto.getPayment().getPaymentMethod(), new Font(Font.FontFamily.HELVETICA, 12));
        document.add(paymentMethod);

        Paragraph paymentStatus = new Paragraph("Status: " + orderDto.getPayment().getStatus(), new Font(Font.FontFamily.HELVETICA, 12));
        document.add(paymentStatus);

        document.add(Chunk.NEWLINE);
    }

    private void addOrderFooter(Document document) throws DocumentException {
        // Thank you message
        Paragraph thankYouMessage = new Paragraph("Thank you for your purchase!", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC));
        thankYouMessage.setAlignment(Element.ALIGN_CENTER);
        thankYouMessage.setSpacingBefore(10f); // Add some space before the paragraph
        document.add(thankYouMessage);

        // Separator line
        LineSeparator separator = new LineSeparator();
        separator.setPercentage(50); // 50% width of the page
        separator.setAlignment(LineSeparator.ALIGN_CENTER);
        separator.setLineColor(BaseColor.DARK_GRAY);
        separator.setLineWidth(1f);
        document.add(new Chunk(separator));

        // All rights reserved notice
        Paragraph rightsNotice = new Paragraph("All rights reserved © macolam.com", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
        rightsNotice.setAlignment(Element.ALIGN_CENTER);
        rightsNotice.setSpacingBefore(5f); // Add some space before the paragraph
        document.add(rightsNotice);
    }
    private void addTableHeader(PdfPTable table, String[] columnHeaders) {
        for (String header : columnHeaders) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }
}

