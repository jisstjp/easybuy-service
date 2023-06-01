package com.stock.inventorymanagement.controllers;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String uploadImageToS3(@RequestParam("file") MultipartFile file) throws IOException {
	File tempFile = File.createTempFile("image", file.getOriginalFilename());
	file.transferTo(tempFile);

	String keyName = file.getOriginalFilename();

	String imageUrl = s3Service.uploadFileToS3(tempFile, keyName);

	tempFile.delete(); // Cleanup temporary file

	return imageUrl;
    }

}
