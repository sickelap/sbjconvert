package com.example.sbjconvert.configuration;

import com.example.sbjconvert.validator.RequestEntryValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ValidationConfiguration {
    @Value("${validation.enabled}")
    private boolean validationEnabled;

    @Bean
    public RequestEntryValidator requestEntryValidator() {
        return new RequestEntryValidator();
    }
}
