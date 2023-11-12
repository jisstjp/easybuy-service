package com.stock.inventorymanagement.service;

import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface IPdfGenerationService {

    byte[] generateOrderSummaryPreviewPdf(Long cartId);

    byte[] generateOrderSummaryPdf(Long orderId) throws DocumentException, IOException;
}
