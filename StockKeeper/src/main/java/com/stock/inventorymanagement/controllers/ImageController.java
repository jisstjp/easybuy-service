package com.stock.inventorymanagement.controllers;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stock.inventorymanagement.service.S3Service;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final S3Service s3Service;

    @Autowired
    public ImageController(S3Service s3Service) {
	this.s3Service = s3Service;
    }
    
    @PostMapping
     public ResponseEntity<String> uploadImageToS3(@RequestParam("file") MultipartFile file) {
        try {
            // Check if file size exceeds the limit (in bytes)
            long fileSizeLimit = 10 * 1024 * 1024; // 10MB 
            if (file.getSize() > fileSizeLimit) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size exceeds the allowed limit");
            }

            File tempFile = File.createTempFile("image", file.getOriginalFilename());
            file.transferTo(tempFile);

            String imageUrl = s3Service.uploadFileToS3(tempFile, file.getOriginalFilename());

            tempFile.delete(); // Cleanup temporary file

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            // Handle IO exception (file transfer, temporary file creation, etc.)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        } catch (Exception e) {
            // Handle any other general exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image upload");
        }
    }

}
