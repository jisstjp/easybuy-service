package com.stock.inventorymanagement.dto;

import java.time.LocalDate;
import java.util.List;

public class CustomerSalesPersonDTO {
    private Long id;  // This might be used for referencing existing relationships if needed.
    private List<Long> customerIds;  // To support multiple customer relationships
    private Long salesPersonId;  // ID of the salesperson
    private LocalDate assignedDate;  // The date when the relationship was assigned

    // Constructors
    public CustomerSalesPersonDTO() {
    }

    public CustomerSalesPersonDTO(Long id, List<Long> customerIds, Long salesPersonId, LocalDate assignedDate) {
        this.id = id;
        this.customerIds = customerIds;
        this.salesPersonId = salesPersonId;
        this.assignedDate = assignedDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public Long getSalesPersonId() {
        return salesPersonId;
    }

    public void setSalesPersonId(Long salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
}
