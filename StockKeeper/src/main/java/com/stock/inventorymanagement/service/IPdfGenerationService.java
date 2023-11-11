package com.stock.inventorymanagement.service;

public interface IPdfGenerationService {

    byte[] generateOrderSummaryPdf(Long cartId);
}
