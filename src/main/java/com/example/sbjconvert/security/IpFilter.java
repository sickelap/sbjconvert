package com.example.sbjconvert.security;

import com.example.sbjconvert.model.GeoLocationResponse;
import com.example.sbjconvert.service.GeoLocationConfiguration;
import com.example.sbjconvert.service.GeoLocationService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class IpFilter implements Filter {

    private final GeoLocationService geoLocationService;
    private final GeoLocationConfiguration geoLocationConfiguration;
    private boolean skipFilter = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        if (!geoLocationConfiguration.isEnabled()) {
            log.info("GeoLocation blocking is disabled");
            skipFilter = true;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (skipFilter) {
            chain.doFilter(request, response);
            return;
        }

        var httpResponse = (HttpServletResponse) response;
        var httpRequest = (HttpServletRequest) request;
        String ip = httpRequest.getRemoteAddr();
        ip = "7.7.7.7";

        log.info("Checking IP address: {}", ip);
        var geoLocationDetails = geoLocationService.getDetails(ip);
        httpRequest.setAttribute("geoLocationDetails", geoLocationDetails);
        if (isBlocked(geoLocationDetails)) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public boolean isBlocked(GeoLocationResponse geoLocationDetails) {
        if (geoLocationDetails == null || geoLocationDetails.getStatus().equals("fail")) {
            return true;
        }
        return isCountryBlocked(geoLocationDetails.getCountryCode()) || isProviderBlocked(geoLocationDetails.getIsp());
    }

    boolean isProviderBlocked(String provider) {
        var blockedProviders = geoLocationConfiguration.getBlockedProviders();
        if (blockedProviders == null || blockedProviders.isEmpty()) {
            return false;
        }
        var result = blockedProviders.contains(provider);
        if (result) {
            log.info("Provider {} is blocked", provider);
        }
        return result;
    }

    boolean isCountryBlocked(String countryCode) {
        var blockedCountries = geoLocationConfiguration.getBlockedCountries();
        if (blockedCountries == null || blockedCountries.isEmpty()) {
            return false;
        }
        var result = blockedCountries.contains(countryCode);
        if (result) {
            log.info("Country {} is blocked", countryCode);
        }
        return result;
    }
}
