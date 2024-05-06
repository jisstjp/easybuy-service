package com.stock.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.Date;

public class CreditDto {

    private Long customerId;
    private BigDecimal amount;
    private String type;
    private String status;
    private Date issueDate;
    private Date expiryDate;
    private String creditDescription;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCreditDescription() {
        return creditDescription;
    }

    public void setCreditDescription(String creditDescription) {
        this.creditDescription = creditDescription;
    }
}
