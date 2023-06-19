package com.stock.inventorymanagement.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stock.inventorymanagement.service.S3Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements S3Service {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String bucketName;

    public S3ServiceImpl(@Value("${aws.accessKey}") String accessKey, @Value("${aws.secretKey}") String secretKey,
	    @Value("${aws.region}") String region, @Value("${aws.bucketName}") String bucketName) {
	this.accessKey = accessKey;
	this.secretKey = secretKey;
	this.region = region;
	this.bucketName = bucketName;
    }

    @Override
    public String uploadFileToS3(File file, String keyName) {
	S3Client s3Client = S3Client.builder().region(Region.of(region))
		.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
		.build();

	String uniqueKeyName = generateUniqueKeyName(keyName);

	PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(uniqueKeyName).build();

	s3Client.putObject(objectRequest, file.toPath());

	return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + uniqueKeyName;
    }

    private String generateUniqueKeyName(String originalKeyName) {
	String uniqueString = String.valueOf(System.currentTimeMillis());
	return originalKeyName + "_" + uniqueString;
    }

}
