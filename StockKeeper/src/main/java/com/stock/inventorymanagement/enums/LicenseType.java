package com.stock.inventorymanagement.enums;

public enum LicenseType {
    TOBACCO("Tobacco License"),
    E_CIGARETTE("E-Cigarette License");

    private final String description;

    // Enum constructor
    LicenseType(String description) {
        this.description = description;
    }

    // Getter for the description
    public String getDescription() {
        return this.description;
    }
}
