package com.example.sbjconvert.security;

import com.example.sbjconvert.model.GeoLocationResponse;
import com.example.sbjconvert.service.GeoLocationConfiguration;
import com.example.sbjconvert.service.GeoLocationService;
import com.example.sbjconvert.service.RequestLogService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.sbjconvert.common.Constants.REQUEST_END_TIME_ATTR;
import static com.example.sbjconvert.common.Constants.REQUEST_START_TIME_ATTR;

@Component
@RequiredArgsConstructor
@Slf4j
public class IpFilter implements Filter {

    private final GeoLocationService geoLocationService;
    private final GeoLocationConfiguration geoLocationConfiguration;
    private final RequestLogService requestLogService;
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

        // add timestamp here because filter is executed before the request interceptor
        request.setAttribute(REQUEST_START_TIME_ATTR, System.currentTimeMillis());

        var httpResponse = (HttpServletResponse) response;
        var httpRequest = (HttpServletRequest) request;

        String ip = httpRequest.getRemoteAddr();

        log.info("Checking IP address: {}", ip);
        var geoLocationDetails = geoLocationService.getDetails(ip);
        httpRequest.setAttribute("geoLocationDetails", geoLocationDetails);

        if (geoLocationConfiguration.getAllowedIps().contains(ip)) {
            log.info("IP address {} is allowed, not logging", ip);
            return;
        }

        if (isBlocked(geoLocationDetails)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            httpRequest.setAttribute(REQUEST_END_TIME_ATTR, System.currentTimeMillis());
            // log request before failing the filter
            requestLogService.log(httpRequest, httpResponse);
            return;
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
