package com.stock.inventorymanagement.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.stock.inventorymanagement.dto.*;
import com.stock.inventorymanagement.exception.UnauthorizedException;
import com.stock.inventorymanagement.service.IPdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public byte[] generateOrderSummaryPreviewPdf(Long cartId, Long userId, boolean isAdminOrManager)  throws UnauthorizedException  {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();


            // Header with Company Name and Date
            addHeader(document, "macolam", dateFormat.format(new Date()));

            // Fetching Cart and Customer Details
            CartDto cartDto = cartService.getCartById(cartId);
            Long cartUserId = cartDto.getUserId();
            if (!userId.equals(cartUserId) && !isAdminOrManager) {
                throw new UnauthorizedException("You are not authorized for this operation");
            }

            CustomerDto customerDto = customerService.getCustomerByUserId(cartUserId);

            // Beautified Customer Details
            addCustomerDetails(document, customerDto);

            // Adding Cart Items and Calculating Prices
            addCartItems(document, cartId);

            // Footer
            addFooter(document);

        }  catch (DocumentException e) {
            throw new RuntimeException("Error while generating PDF", e);
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }



    private void addHeader(Document document, String companyName, String dateTime) throws DocumentException {
        try {
            // Assuming the logo is stored in the resources directory of your project
            Image logo = Image.getInstance(getClass().getResource("/images/macolam.logo.png"));
            logo.scaleToFit(50, 50); // Adjust the size of the logo as needed

            // Create a table for the header with 3 columns
            PdfPTable headerTable = new PdfPTable(new float[]{1, 4, 2});
            headerTable.setWidthPercentage(100); // Set width to 100% of the page

            // Add logo to the first cell with padding for a cleaner look
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setPaddingLeft(10);
            logoCell.setPaddingTop(10);
            headerTable.addCell(logoCell);

            // Add a blank cell as a spacer
            PdfPCell spacerCell = new PdfPCell();
            spacerCell.setBorder(Rectangle.NO_BORDER);
            spacerCell.setColspan(2); // Span across the rest of the row
            headerTable.addCell(spacerCell);

            // Add company name and date in the last cell, with custom fonts and alignment
            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            textCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            textCell.setPaddingRight(10);

            // Custom fonts for company name and date
            Font companyNameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Font dateTimeFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Add company name and date to the cell
            Paragraph textParagraph = new Paragraph();
            textParagraph.add(new Phrase(companyName.toUpperCase() + "\n", companyNameFont)); // Company name in uppercase
            textParagraph.add(new Phrase(dateTime, dateTimeFont)); // Date in a smaller font
            textParagraph.setAlignment(Element.ALIGN_RIGHT);
            textCell.addElement(textParagraph);

            // Adjust cell padding to position the text nicely
            textCell.setPaddingTop(logo.getScaledHeight() - textParagraph.getFont().getSize()); // Align text top with logo top

            headerTable.addCell(textCell);

            // Add the complete header table to the document
            document.add(headerTable);

            // Add a spacer paragraph for more space below the header
            document.add(new Paragraph(" "));
        } catch (BadElementException | IOException ex) {
            throw new DocumentException("Error adding header: " + ex.getMessage(), ex);
        }
    }





    private void addCustomerDetails(Document document, CustomerDto customerDto) throws DocumentException {
        Font customerDetailsHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
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
        List<CartItemDto> cartItems = cartItemService.getCartItemsByCartId(cartId);
        PdfPTable table = new PdfPTable(new float[]{3f, 1.2f, 2f, 2f, 3f}); // Append 'f' to make the numbers float literals // Adjust column widths for the new header
        table.setWidthPercentage(100);

        // Define table headers with improved styling
        Stream.of("Product Name", "Quantity", "Price per Item", "Subtotal", "Suggested Price per Item") // Updated header title
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell(new Phrase(columnTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY); // Light gray background for header
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setPadding(5); // Add padding for better aesthetics
                    table.addCell(header);
                });

        BigDecimal total = BigDecimal.ZERO;

        // Adding cart items with enhanced styling
        for (CartItemDto item : cartItems) {
            String productName = productService.getProductNameById(item.getProductId());
            BigDecimal price = safelyFetchSalesPrice(item.getProductId(), item.getPrice());
            BigDecimal suggestedPrice = fetchSuggestedPrice(item.getProductId(), price);
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            // Add product details to the table with improved styling
            addTableCell(table, productName, Element.ALIGN_LEFT);
            addTableCell(table, String.valueOf(item.getQuantity()), Element.ALIGN_CENTER);
            addTableCell(table, "$" + price.toPlainString(), Element.ALIGN_RIGHT);
            addTableCell(table, "$" + lineTotal.toPlainString(), Element.ALIGN_RIGHT);
            addTableCell(table, "$" + suggestedPrice.toPlainString(), Element.ALIGN_RIGHT);

            total = total.add(lineTotal);
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Total Cost: $" + total.toPlainString(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(Chunk.NEWLINE);
    }

    private void addTableCell(PdfPTable table, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(3); // Add padding for a neat layout
        cell.setBorderWidth(0.5f); // Set a subtle border width
        table.addCell(cell);
    }


    private BigDecimal safelyFetchSalesPrice(Long productId, BigDecimal fallbackPrice) {
        try {
            return productService.getSalesPrice(productId);
        } catch (Exception e) {
            // Log the exception if necessary
            return fallbackPrice;
        }
    }

    private BigDecimal fetchSuggestedPrice(Long productId,BigDecimal fallbackPrice) {
        try {
            return productService.getSuggestedPrice(productId);
        } catch (Exception e) {
            return BigDecimal. ZERO;
        }
    }


    private void addFooter(Document document) throws DocumentException {
        LineSeparator separator = new LineSeparator();
        separator.setPercentage(80);
        separator.setAlignment(Element.ALIGN_CENTER);
        separator.setLineColor(new BaseColor(80, 99, 101)); // Dark gray-blue color for the line

        // Add the line separator
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE); // Spacer

        // Footer content
        Font footerTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new BaseColor(80, 99, 101));
        Font footerContentFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new BaseColor(80, 99, 101));

        // Contact Title
        Paragraph contactTitle = new Paragraph("Contact us:", footerTitleFont);
        contactTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(contactTitle);

        // Phone information
        Paragraph phoneInfo = new Paragraph("Phone: +1 (424) 230-6025", footerContentFont);
        phoneInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(phoneInfo);

        // Email information
        Paragraph emailInfo = new Paragraph("Email: mathew@macolam.com", footerContentFont);
        emailInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(emailInfo);

        // Spacer between contact info and copyright
       // document.add(Chunk.NEWLINE);

        // Copyright information
        Paragraph copyrightInfo = new Paragraph("All rights reserved © macolam.com", footerContentFont);
        copyrightInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(copyrightInfo);
    }


    @Override
    public byte[] generateOrderSummaryPdf(Long orderId, Long userId, boolean isAdminOrManager) throws DocumentException, IOException {
        OrderDto orderDto = orderService.getOrder(orderId);
        //Check if the userId is the same as the JWT user id and if the user is an admin
        if (!userId.equals(orderDto.getUserId()) && !isAdminOrManager) {
            throw new UnauthorizedException("You are not authorized for this operation");
        }

        CustomerDto customerDto = customerService.getCustomerByUserId(orderDto.getUserId());


        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addCompanyHeader(document, "macolam");
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

/*
    private void addCompanyHeader(Document document, String companyName) throws DocumentException, IOException {
        // Format the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(new Date());

        // Create a table for the header with three columns
        PdfPTable headerTable = new PdfPTable(2); // Two columns for logo and company name with date
        headerTable.setWidthPercentage(100);

        // Load the company logo image using ClassPathResource
        ClassPathResource logoResource = new ClassPathResource("/images/macolam.logo.png");
        Path logoPath = Files.createTempFile("logo", ".png");
        Files.copy(logoResource.getInputStream(), logoPath, StandardCopyOption.REPLACE_EXISTING);
        Image logoImage = Image.getInstance(logoPath.toAbsolutePath().toString());
        logoImage.scaleToFit(50, 50); // Adjust the size of the logo as needed
        PdfPCell logoCell = new PdfPCell(logoImage, false);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPaddingLeft(10);
        logoCell.setPaddingTop(10);
        headerTable.addCell(logoCell);

        // Combine the company name and date into one cell and align it to the right
        Font companyFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.DARK_GRAY); // Smaller font size to prevent wrapping
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY); // Smaller font size for date
        // Create a single Paragraph for company name and date
        Paragraph companyAndDate = new Paragraph();
        companyAndDate.add(new Phrase(companyName.toUpperCase() + "\n", companyFont)); // Company name in uppercase for better visibility
        companyAndDate.add(new Phrase(formattedDate, dateFont));
        companyAndDate.setAlignment(Element.ALIGN_RIGHT); // Align the paragraph to the right

        PdfPCell companyAndDateCell = new PdfPCell(companyAndDate);
        companyAndDateCell.setBorder(Rectangle.NO_BORDER);
        companyAndDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        companyAndDateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        companyAndDateCell.setPaddingRight(10);
        headerTable.addCell(companyAndDateCell);

        // Ensure the logo and text are aligned on the same baseline
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        headerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);

        // Add the header table to the document
        document.add(headerTable);
        document.add(Chunk.NEWLINE);

        // Clean up the temporary file
        Files.deleteIfExists(logoPath);
    }
*/

    private void addCompanyHeader(Document document, String companyName) throws DocumentException, IOException {
        // Format the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date());

        // Create a table for the header with two columns
        PdfPTable headerTable = new PdfPTable(2); // Two columns for logo and company name with date
        headerTable.setWidthPercentage(100);

        // Load the company logo image using ClassPathResource
        ClassPathResource logoResource = new ClassPathResource("/images/macolam.logo.png");
        Path logoPath = Files.createTempFile("logo", ".png");
        Files.copy(logoResource.getInputStream(), logoPath, StandardCopyOption.REPLACE_EXISTING);
        Image logoImage = Image.getInstance(logoPath.toAbsolutePath().toString());
        logoImage.scaleToFit(50, 50); // Adjust the size of the logo as needed
        PdfPCell logoCell = new PdfPCell(logoImage, false);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPaddingLeft(10);
        logoCell.setPaddingTop(10);
        headerTable.addCell(logoCell);

        // Enhance the company name and date format
        Font companyFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0, 102, 204)); // Larger font size, bold and colored
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY); // Slightly larger font for date, gray color

        // Create a single Paragraph for company name and date
        Paragraph companyAndDate = new Paragraph();
        companyAndDate.add(new Phrase(companyName.toUpperCase() + "\n", companyFont)); // Company name in uppercase for better visibility
        companyAndDate.add(new Phrase(formattedDate, dateFont));
        companyAndDate.setAlignment(Element.ALIGN_RIGHT); // Align the paragraph to the right

        PdfPCell companyAndDateCell = new PdfPCell(companyAndDate);
        companyAndDateCell.setBorder(Rectangle.NO_BORDER);
        companyAndDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        companyAndDateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        companyAndDateCell.setPaddingRight(10);
        headerTable.addCell(companyAndDateCell);

        // Ensure the logo and text are aligned on the same baseline
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        headerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);

        // Add the header table to the document
        document.add(headerTable);
        document.add(Chunk.NEWLINE);

        // Clean up the temporary file
        Files.deleteIfExists(logoPath);
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
        Paragraph title = new Paragraph("Customer Details", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK));
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



/*
    private void addOrderDetails(Document document, OrderDto orderDto) throws DocumentException {
        // Adding a bit more space before the title
        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));

        Paragraph title = new Paragraph("Order Details", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingBefore(10f); // Adding space before the title
        document.add(title);

        // Creating the table for order details with adjusted column widths
        PdfPTable table = new PdfPTable(new float[]{4f, 1.3f, 2f, 2f, 3.4f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f); // Adding space before the table
        addTableHeader(table, new String[]{"Item", "Quantity", "Price", "Subtotal", "Suggested Price per Item"}); // Corrected header titles

        // Populating the table with order item details
        for (OrderItemDto item : orderDto.getOrderItems()) {
            String productName = productService.getProductNameById(item.getProductId());
            // Assuming the method exists to fetch the suggested price
            BigDecimal suggestedPrice = fetchSuggestedPrice(item.getProductId(), item.getPrice());
            BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));

            // Add table cells for each order item
            table.addCell(new PdfPCell(new Phrase(productName, new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase("$" + item.getPrice().toPlainString(), new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase("$" + subtotal.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase("$" + suggestedPrice.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12))));
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

*/

    private void addOrderDetails(Document document, OrderDto orderDto) throws DocumentException {
        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));

        Paragraph title = new Paragraph("Order Details", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingBefore(10f);
        document.add(title);

        PdfPTable table = new PdfPTable(new float[]{4f, 1.8f, 2f, 2f, 3.4f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        addTableHeader(table, new String[]{"Item", "Quantity", "Price", "Subtotal", "Suggested Price per Item"});

        // Alternate row color for better readability
        boolean alternateColor = false;
        BaseColor lightGray = new BaseColor(240, 240, 240); // Light gray color

        for (OrderItemDto item : orderDto.getOrderItems()) {
            String productName = productService.getProductNameById(item.getProductId());
            BigDecimal suggestedPrice = fetchSuggestedPrice(item.getProductId(), item.getPrice());
            BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));

            PdfPCell cell;

            cell = new PdfPCell(new Phrase(productName, new Font(Font.FontFamily.HELVETICA, 12)));
            cell.setBackgroundColor(alternateColor ? lightGray : BaseColor.WHITE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), new Font(Font.FontFamily.HELVETICA, 12)));
            cell.setBackgroundColor(alternateColor ? lightGray : BaseColor.WHITE);
            table.addCell(cell);

            cell = createPriceCell(item.getPrice());
            cell.setBackgroundColor(alternateColor ? lightGray : BaseColor.WHITE);
            table.addCell(cell);

            cell = createPriceCell(subtotal);
            cell.setBackgroundColor(alternateColor ? lightGray : BaseColor.WHITE);
            table.addCell(cell);

            cell = createPriceCell(suggestedPrice);
            cell.setBackgroundColor(alternateColor ? lightGray : BaseColor.WHITE);
            table.addCell(cell);

            alternateColor = !alternateColor; // Toggle the color for the next row
        }
        document.add(table);

        Paragraph total = new Paragraph("Total Price: " + orderDto.getTotalPrice().toPlainString(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(5f);
        document.add(total);

        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));
    }

    private PdfPCell createPriceCell(BigDecimal amount) {
        PdfPCell cell = new PdfPCell(new Phrase("$" + amount.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingRight(10); // Add padding for better alignment
        return cell;
    }
    private void addTableHeader(PdfPTable table, String[] headerTitles) {
        for (String headerTitle : headerTitles) {
            PdfPCell header = new PdfPCell(new Phrase(headerTitle, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.setPadding(5);
            table.addCell(header);
        }
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

        // Contact information
        Paragraph contactInfo = new Paragraph("Contact us at:", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
        contactInfo.setAlignment(Element.ALIGN_CENTER);
        //contactInfo.setSpacingBefore(5f); // Add some space before the contact info
        document.add(contactInfo);

        // Email address
        Paragraph email = new Paragraph("Email: mathew@macolam.com", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
        email.setAlignment(Element.ALIGN_CENTER);
       // email.setSpacingBefore(5f); // Add some space before the email address
        document.add(email);

        // Phone number
        Paragraph phone = new Paragraph("Phone: +1 (424) 230-6025", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
        phone.setAlignment(Element.ALIGN_CENTER);
       // phone.setSpacingBefore(5f); // Add some space before the phone number
        document.add(phone);

        // All rights reserved notice
        Paragraph rightsNotice = new Paragraph("All rights reserved © macolam.com", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
        rightsNotice.setAlignment(Element.ALIGN_CENTER);
        //rightsNotice.setSpacingBefore(5f); // Add some space before the paragraph
        document.add(rightsNotice);
    }

}

