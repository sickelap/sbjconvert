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

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ip = httpRequest.getRemoteAddr();

        log.info("Checking IP address: {}", ip);
        if (isBlocked(ip)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        log.info("IP access allowed for: {}", ip);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public boolean isBlocked(String ip) {
        if (geoLocationConfiguration.getAllowedIps().contains(ip)) {
            return false;
        }
        var geoLocationDetails = geoLocationService.getDetails(ip);
        if (geoLocationDetails == null || geoLocationDetails.getStatus().equals("fail")) {
            return true;
        }
        return isCountryBlocked(geoLocationDetails) || isProviderBlocked(geoLocationDetails);
    }

    boolean isProviderBlocked(GeoLocationResponse geoLocationDetails) {
        var blockedProviders = geoLocationConfiguration.getBlockedProviders();
        if (blockedProviders == null || blockedProviders.isEmpty()) {
            return false;
        }
        var result = blockedProviders.contains(geoLocationDetails.getIsp());
        if (result) {
            log.info("Provider {} is blocked", geoLocationDetails.getIsp());
        }
        return result;
    }

    boolean isCountryBlocked(GeoLocationResponse geoLocationDetails) {
        var blockedCountries = geoLocationConfiguration.getBlockedCountries();
        if (blockedCountries == null || blockedCountries.isEmpty()) {
            return false;
        }
        var result = blockedCountries.contains(geoLocationDetails.getCountryCode());
        if (result) {
            log.info("Country {} is blocked", geoLocationDetails.getCountryCode());
        }
        return result;
    }
}
