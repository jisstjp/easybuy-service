package com.stock.inventorymanagement.service;

import java.io.File;

public interface S3Service {
    
    public String uploadFileToS3(File file, String keyName);

}
