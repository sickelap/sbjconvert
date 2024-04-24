package com.example.sbjconvert.validator;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ValidationConfig {
    @Value("${validation.enabled}")
    private boolean validationEnabled;
}
