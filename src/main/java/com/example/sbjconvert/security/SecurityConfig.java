package com.example.sbjconvert.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public FilterRegistrationBean<IpFilter> FilterRegistrationBeanIpFilter(IpFilter ipFilter) {
        FilterRegistrationBean<IpFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(ipFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
