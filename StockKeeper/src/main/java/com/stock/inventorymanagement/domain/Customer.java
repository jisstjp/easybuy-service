package com.stock.inventorymanagement.domain;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "newsletter")
    private boolean newsletter;

    @Column(name = "tax_file")
    private String taxFile;

    @Column(name = "website")
    private String website;

    @Column(name = "phone")
    private String phone;

    @Column(name = "tax_id_number")
    private String taxIdNumber;

    @Column(name = "company")
    private String company;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @Column(name = "zip_postal_code")
    private String zipPostalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_setup")
    private String businessSetup;

    @Column(name = "business_organization")
    private String businessOrganization;

    @Column(name = "years_in_business")
    private Integer yearsInBusiness;

    @Column(name = "locations")
    private Integer locations;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "sales_channel")
    private String salesChannel;

    @Column(name = "how_did_you_hear")
    private String howDidYouHear;

    @Column(name = "monthly_inventory_budget")
    private BigDecimal monthlyInventoryBudget;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "approval_status")
    private String approvalStatus;

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

    public Long getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
	this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
	this.updatedBy = updatedBy;
    }

    public Boolean getIsDeleted() {
	return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
	this.isDeleted = isDeleted;
    }

    public String getApprovalStatus() {
	return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
	this.approvalStatus = approvalStatus;
    }

}
