package com.stock.inventorymanagement.dto;

import com.stock.inventorymanagement.enums.SortDirection;

public class SortCriteria {

    private String sortBy;
    private SortDirection sortDirection;

    public SortCriteria() {
    }

    public SortCriteria(String sortBy, SortDirection sortDirection) {
	this.sortBy = sortBy;
	this.sortDirection = sortDirection;
    }

    public String getSortBy() {
	return sortBy;
    }

    public void setSortBy(String sortBy) {
	this.sortBy = sortBy;
    }

    public SortDirection getSortDirection() {
	return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
	this.sortDirection = sortDirection;
    }

}
