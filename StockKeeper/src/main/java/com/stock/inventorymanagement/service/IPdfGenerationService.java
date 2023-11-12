package com.stock.inventorymanagement.service;

import com.itextpdf.text.DocumentException;
import com.stock.inventorymanagement.exception.UnauthorizedException;

import java.io.IOException;

public interface IPdfGenerationService {

    byte[] generateOrderSummaryPreviewPdf(Long cartId,Long userId,boolean isAdminOrManager) throws UnauthorizedException;

    byte[] generateOrderSummaryPdf(Long orderId,Long userId,boolean isAdminOrManager) throws DocumentException, IOException;
}
