package com.stock.inventorymanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReturnDTO {

    private Long id;
    private Long orderId;
    private String status;
    private String reason;
    private String comments;
    private BigDecimal creditAmount;
    private List<ReturnItemDTO> returnItems;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public List<ReturnItemDTO> getReturnItems() {
        return returnItems;
    }

    public void setReturnItems(List<ReturnItemDTO> returnItems) {
        this.returnItems = returnItems;
    }
}
