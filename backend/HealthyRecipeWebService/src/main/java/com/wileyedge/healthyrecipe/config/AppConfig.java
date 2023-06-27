package com.wileyedge.healthyrecipe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
public class AppConfig {

//	private String accessKey = "AKIAWROIHFOGLFZYUDEF";
//
//
//	private String accessSecret = "hCrIezmTRF3LS9fRpDoaue2IaulhVbC08zj2LUEa";
	
	private String accessKey = "AKIAWROIHFOGMUFK6AOS";


	private String accessSecret = "XyE1XE5kL9aLbCnr/RJXe/+bojiefDHQNtiDPR4D";


	@Bean
	public S3Client generateS3Client() {
		AwsCredentials credentials = AwsBasicCredentials.create(accessKey, accessSecret);
		return S3Client.builder()
				.region(Region.AP_SOUTHEAST_2)
				.credentialsProvider(StaticCredentialsProvider.create(credentials))
				.build();
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(10485760); // Set the maximum file size allowed (in bytes)
		return resolver;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
