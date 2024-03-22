package com.stock.inventorymanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private boolean newsletter;
    private String taxFile;
    private String website;
    private String phone;
    @JsonIgnore
    private String taxIdNumber;
    private String company;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String zipPostalCode;
    private String country;
    private String email;
    private String password;
    private String businessName;
    private String businessSetup;
    private String businessOrganization;
    private Integer yearsInBusiness;
    private Integer locations;
    private String businessType;
    private String salesChannel;
    private String howDidYouHear;
    private BigDecimal monthlyInventoryBudget;
    private String approvalStatus;

    private List<CustomerLicenseDto> customerLicenseDto;

    private BigDecimal storeCredit;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public boolean isNewsletter() {
	return newsletter;
    }

    public void setNewsletter(boolean newsletter) {
	this.newsletter = newsletter;
    }

    public String getTaxFile() {
	return taxFile;
    }

    public void setTaxFile(String taxFile) {
	this.taxFile = taxFile;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getTaxIdNumber() {
	return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
	this.taxIdNumber = taxIdNumber;
    }

    public String getCompany() {
	return company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public String getAddressLine1() {
	return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
	this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
	return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
	this.addressLine2 = addressLine2;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getStateProvince() {
	return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
	this.stateProvince = stateProvince;
    }

    public String getZipPostalCode() {
	return zipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
	this.zipPostalCode = zipPostalCode;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getBusinessName() {
	return businessName;
    }

    public void setBusinessName(String businessName) {
	this.businessName = businessName;
    }

    public String getBusinessSetup() {
	return businessSetup;
    }

    public void setBusinessSetup(String businessSetup) {
	this.businessSetup = businessSetup;
    }

    public String getBusinessOrganization() {
	return businessOrganization;
    }

    public void setBusinessOrganization(String businessOrganization) {
	this.businessOrganization = businessOrganization;
    }

    public Integer getYearsInBusiness() {
	return yearsInBusiness;
    }

    public void setYearsInBusiness(Integer yearsInBusiness) {
	this.yearsInBusiness = yearsInBusiness;
    }

    public Integer getLocations() {
	return locations;
    }

    public void setLocations(Integer locations) {
	this.locations = locations;
    }

    public String getBusinessType() {
	return businessType;
    }

    public void setBusinessType(String businessType) {
	this.businessType = businessType;
    }

    public String getSalesChannel() {
	return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
	this.salesChannel = salesChannel;
    }

    public String getHowDidYouHear() {
	return howDidYouHear;
    }

    public void setHowDidYouHear(String howDidYouHear) {
	this.howDidYouHear = howDidYouHear;
    }

    public BigDecimal getMonthlyInventoryBudget() {
	return monthlyInventoryBudget;
    }

    public void setMonthlyInventoryBudget(BigDecimal monthlyInventoryBudget) {
	this.monthlyInventoryBudget = monthlyInventoryBudget;
    }

    public String getApprovalStatus() {
	return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
	this.approvalStatus = approvalStatus;
    }

    public List<CustomerLicenseDto> getCustomerLicenseDto() {
        return customerLicenseDto;
    }

    public void setCustomerLicenseDto(List<CustomerLicenseDto> customerLicenseDto) {
        this.customerLicenseDto = customerLicenseDto;
    }

    public BigDecimal getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(BigDecimal storeCredit) {
        this.storeCredit = storeCredit;
    }
}
