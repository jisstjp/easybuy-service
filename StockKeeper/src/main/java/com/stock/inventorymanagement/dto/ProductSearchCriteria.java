package com.stock.inventorymanagement.dto;

import java.util.List;

public class ProductSearchCriteria {

    private String keyword;
    private List<FieldCriterion<?>> fieldCriteria;

    private SortCriteria sortCriteria;
    private int page;
    private int size;

    public String getKeyword() {
	return keyword;
    }

    public void setKeyword(String keyword) {
	this.keyword = keyword;
    }

    public List<FieldCriterion<?>> getFieldCriteria() {
	return fieldCriteria;
    }

    public void setFieldCriteria(List<FieldCriterion<?>> fieldCriteria) {
	this.fieldCriteria = fieldCriteria;
    }

    public SortCriteria getSortCriteria() {
	return sortCriteria;
    }

    public void setSortCriteria(SortCriteria sortCriteria) {
	this.sortCriteria = sortCriteria;
    }

    public int getPage() {
	return page;
    }

    public void setPage(int page) {
	this.page = page;
    }

    public int getSize() {
	return size;
    }

    public void setSize(int size) {
	this.size = size;
    }

}
