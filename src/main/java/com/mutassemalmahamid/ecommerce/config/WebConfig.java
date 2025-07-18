package com.mutassemalmahamid.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Web configuration class to handle Spring Data web support
 * and fix PageImpl serialization warnings
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {
    // This configuration enables proper page serialization
    // and eliminates the PageImpl serialization warning
}
