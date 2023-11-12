package com.stock.inventorymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
@Configuration
public class PdfConfig {
    @Bean
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }
}
