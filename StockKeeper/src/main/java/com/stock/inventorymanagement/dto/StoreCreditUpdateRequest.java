package com.stock.inventorymanagement.dto;
import java.math.BigDecimal;

public class StoreCreditUpdateRequest {

    private BigDecimal storeCredit;

    public BigDecimal getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(BigDecimal storeCredit) {
        this.storeCredit = storeCredit;
    }
}
