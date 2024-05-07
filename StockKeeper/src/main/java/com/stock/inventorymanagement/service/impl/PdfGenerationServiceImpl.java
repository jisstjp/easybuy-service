package com.stock.inventorymanagement.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.dto.*;
import com.stock.inventorymanagement.exception.UnauthorizedException;
import com.stock.inventorymanagement.repository.CustomerRepository;
import com.stock.inventorymanagement.service.CreditService;
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
import java.util.Optional;
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

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditService creditService;

    @Override
    public byte[] generateOrderSummaryPreviewPdf(Long cartId, Long userId, boolean isAdminOrManager,boolean storeCreditAdd)  throws UnauthorizedException  {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();


            // Header with Company Name and Date
            addHeader(document);

            addProformaInvoiceTitle(document);

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
            addCartItems(document, cartId,storeCreditAdd,userId);

            // Footer
            addFooter(document);

        }  catch (DocumentException e) {
            throw new RuntimeException("Error while generating PDF", e);
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }



    private void addHeader(Document document) throws DocumentException {
        try {
            // Load the company logo
            Image logo = Image.getInstance(getClass().getResource("/images/macolam.logo.png"));
            // Calculate the new width to maintain the aspect ratio
            float scaler = 80 / logo.getHeight(); // Assuming you want the height to be 80 units
            logo.scaleAbsoluteHeight(80);
            logo.scaleAbsoluteWidth(logo.getWidth() * scaler);

            // Create a table with a single column for the logo to be left-aligned
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100); // Set the table width to 100% of the page width
            headerTable.setHorizontalAlignment(Element.ALIGN_LEFT); // Align the table to the left

            // Create a cell for the logo with no border
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setPaddingLeft(5);
            logoCell.setPaddingTop(5); // Adjust padding as needed
            headerTable.addCell(logoCell);

            document.add(headerTable);
        } catch (BadElementException | IOException ex) {
            throw new DocumentException("Error adding header: " + ex.getMessage(), ex);
        }
    }




    private void addProformaInvoiceTitle(Document document) throws DocumentException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String currentDate = dateFormat.format(new Date());

        // Add the "Proforma Invoice" title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Proforma Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add the date below the title
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 12);
        Paragraph dateParagraph = new Paragraph(currentDate, dateFont);
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(dateParagraph);

        // Add a spacer to provide some space after the title
        document.add(new Paragraph(" "));
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


    private void addCartItems(Document document, Long cartId, boolean storeCreditApplied, Long userId) throws DocumentException {
        List<CartItemDto> cartItems = cartItemService.getCartItemsByCartId(cartId);
        PdfPTable table = new PdfPTable(new float[]{4f, 2f, 2f, 2f, 2f});
        table.setWidthPercentage(100);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        BaseColor headerBackgroundColor = new BaseColor(245, 245, 245);

        // Add table headers
        Stream.of("Product Name", "Retail Price", "Quantity", "Price per Item", "Subtotal")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell(new Phrase(columnTitle, headerFont));
                    header.setBackgroundColor(headerBackgroundColor);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setPadding(8);
                    table.addCell(header);
                });

        BigDecimal total = BigDecimal.ZERO;

        // Adding cart items to the table
        for (CartItemDto item : cartItems) {
            String productName = productService.getProductNameById(item.getProductId());
            BigDecimal price = safelyFetchSalesPrice(item.getProductId(), item.getPrice());
            BigDecimal retailPrice = fetchSuggestedPrice(item.getProductId(), price);
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            addTableCell(table, productName, Element.ALIGN_LEFT);
            addTableCell(table, "$" + retailPrice.toPlainString(), Element.ALIGN_RIGHT);
            addTableCell(table, String.valueOf(item.getQuantity()), Element.ALIGN_CENTER);
            addTableCell(table, "$" + price.toPlainString(), Element.ALIGN_RIGHT);
            addTableCell(table, "$" + lineTotal.toPlainString(), Element.ALIGN_RIGHT);

            total = total.add(lineTotal);
        }

        document.add(table);

        // Define a font for highlighting
        Font highlightFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

        BigDecimal totalAvailableCredits = BigDecimal.ZERO;
        BigDecimal appliedStoreCredit = BigDecimal.ZERO;

        if (storeCreditApplied) {
            Optional<Customer> optionalCustomer = customerRepository.findByUserId(userId);
            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                totalAvailableCredits = creditService.getTotalCredits(customer.getId());
                appliedStoreCredit = totalAvailableCredits.min(total);
                total = total.subtract(appliedStoreCredit);
            }
        }

        if (storeCreditApplied && totalAvailableCredits.compareTo(BigDecimal.ZERO) > 0) {
            // Display total available store credits
            Paragraph totalAvailableCreditsParagraph = new Paragraph("Total Available Store Credits: $" + totalAvailableCredits.toPlainString(), highlightFont);
            totalAvailableCreditsParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalAvailableCreditsParagraph);

            // Display applied store credit
            if (appliedStoreCredit.compareTo(BigDecimal.ZERO) > 0) {
                Paragraph storeCreditAppliedParagraph = new Paragraph("Store Credit Applied: -$" + appliedStoreCredit.toPlainString(), highlightFont);
                storeCreditAppliedParagraph.setAlignment(Element.ALIGN_RIGHT);
                document.add(storeCreditAppliedParagraph);
            }
        }

        // Display total cost in a highlighted fashion
        Paragraph totalCostParagraph = new Paragraph("Total Cost: $" + total.toPlainString(), highlightFont);
        totalCostParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalCostParagraph);
        document.add(Chunk.NEWLINE);
    }


    private void addTableCell(PdfPTable table, String text, int alignment, int... colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        if (colspan.length > 0) {
            cell.setColspan(colspan[0]);
        }
        table.addCell(cell);
    }


    private void addTableCell(PdfPTable table, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        cell.setBorderWidth(0.5f);
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

            addCompanyHeader(document);
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
    private void addOrderDetails(Document document, OrderDto orderDto) throws DocumentException {
        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));

        Paragraph title = new Paragraph("Order Details", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingBefore(10f);
        document.add(title);

        // Adjusted column widths
        PdfPTable table = new PdfPTable(new float[]{5f, 2f, 2f, 2f, 2f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        // Updated header with "Retail Price" next to "Product"
        addTableHeader(table, new String[]{"Item", "Retail Price", "Quantity", "Price", "Subtotal"});

        boolean alternateColor = false;
        BaseColor lightGray = new BaseColor(240, 240, 240); // Light gray color for alternate rows

        for (OrderItemDto item : orderDto.getOrderItems()) {
            String productName = productService.getProductNameById(item.getProductId());
            BigDecimal suggestedPrice = fetchSuggestedPrice(item.getProductId(), item.getPrice());
            BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));

            table.addCell(createCell(productName, Font.FontFamily.HELVETICA, 12, Element.ALIGN_LEFT, alternateColor ? lightGray : BaseColor.WHITE));
            // Moved retail price cell next to product name
            table.addCell(createPriceCell(suggestedPrice, Element.ALIGN_RIGHT, alternateColor ? lightGray : BaseColor.WHITE));
            table.addCell(createCell(String.valueOf(item.getQuantity()), Font.FontFamily.HELVETICA, 12, Element.ALIGN_RIGHT, alternateColor ? lightGray : BaseColor.WHITE));
            table.addCell(createPriceCell(item.getPrice(), Element.ALIGN_RIGHT, alternateColor ? lightGray : BaseColor.WHITE));
            table.addCell(createPriceCell(subtotal, Element.ALIGN_RIGHT, alternateColor ? lightGray : BaseColor.WHITE));

            alternateColor = !alternateColor; // Toggle the color for the next row
        }
        document.add(table);

        Paragraph total = new Paragraph("Total Price: " + orderDto.getTotalPrice().toPlainString(), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(5f);
        document.add(total);

        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));
    }
*/


    private void addOrderDetails(Document document, OrderDto orderDto) throws DocumentException {
        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));

        Paragraph title = new Paragraph("Order Details", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingBefore(10f);
        document.add(title);

        PdfPTable table = new PdfPTable(new float[]{5f, 2f, 2f, 2f, 2f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        addTableHeader(table, new String[]{"Item", "Retail Price", "Quantity", "Price", "Subtotal"});

        for (OrderItemDto item : orderDto.getOrderItems()) {
            String productName = productService.getProductNameById(item.getProductId());
            BigDecimal suggestedPrice = fetchSuggestedPrice(item.getProductId(), item.getPrice());
            BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));

            // Cells now consistently use a white background color
            table.addCell(createCell(productName, Font.FontFamily.HELVETICA, 12, Element.ALIGN_LEFT, BaseColor.WHITE));

            // Add formatted price cells directly here
            table.addCell(new PdfPCell(new Phrase("$" + suggestedPrice.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase("$" + item.getPrice().toPlainString(), new Font(Font.FontFamily.HELVETICA, 12))));
            table.addCell(new PdfPCell(new Phrase("$" + subtotal.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12))));
        }
        document.add(table);

        Paragraph total = new Paragraph("Total Price: $" + orderDto.getTotalPrice().toPlainString(), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(5f);
        document.add(total);

        if (orderDto.getStoreCreditApplied() != null && orderDto.getStoreCreditApplied().compareTo(BigDecimal.ZERO) > 0) {
            Paragraph storeCredit = new Paragraph("Store Credit Applied: $" + orderDto.getStoreCreditApplied().toPlainString(), new Font(Font.FontFamily.HELVETICA, 12));
            storeCredit.setAlignment(Element.ALIGN_RIGHT);
            document.add(storeCredit);
        }

        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8)));
    }





    private void addTableHeader(PdfPTable table, String[] headerTitles) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell headerCell;

        // Light gray color for a lighter and simpler header
        BaseColor headerBackgroundColor = new BaseColor(230, 230, 230);

        for (String title : headerTitles) {
            headerCell = new PdfPCell(new Phrase(title, headerFont));
            headerCell.setBackgroundColor(headerBackgroundColor); // Set a light gray background
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }
    }

    private PdfPCell createCell(String content, Font.FontFamily fontFamily, int size, int alignment, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, new Font(fontFamily, size)));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    private PdfPCell createPriceCell(BigDecimal price, int alignment, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(price.toPlainString(), new Font(Font.FontFamily.HELVETICA, 12)));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }




    private void addCompanyHeader(Document document) throws DocumentException, IOException {
        // Load the company logo image using ClassPathResource
        ClassPathResource logoResource = new ClassPathResource("/images/macolam.logo.png");
        Image logoImage = Image.getInstance(logoResource.getURL());
        logoImage.scaleAbsoluteHeight(80); // Adjusted logo height
        logoImage.scaleAbsoluteWidth(80 * logoImage.getWidth() / logoImage.getHeight()); // Maintain aspect ratio
        PdfPCell logoCell = new PdfPCell(logoImage, false);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPaddingLeft(10);
        logoCell.setPaddingTop(10);

        // Create a table for the header with a single column for the logo
        PdfPTable headerTable = new PdfPTable(1); // Only one column for the logo
        headerTable.setWidthPercentage(100);
        headerTable.addCell(logoCell);

        // Ensure the logo is aligned on the same baseline
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        headerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);

        // Add the header table to the document
        document.add(headerTable);
        document.add(Chunk.NEWLINE);
    }



    private void addOrderHeader(Document document, OrderDto orderDto) throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formattedDate = orderDto.getCreatedAt().format(formatter);

        // Elegant and concise order summary text
        Paragraph orderHeader = new Paragraph("Order Summary",
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        orderHeader.setAlignment(Element.ALIGN_CENTER);
        document.add(orderHeader);

        // Order details with Order Number first, then Order Placed date
        Paragraph orderDetails = new Paragraph("Order Number: #" + orderDto.getId() +
                "\nOrder Placed: " + formattedDate,
                new Font(Font.FontFamily.HELVETICA, 12));
        orderDetails.setAlignment(Element.ALIGN_CENTER);
        document.add(orderDetails);

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

