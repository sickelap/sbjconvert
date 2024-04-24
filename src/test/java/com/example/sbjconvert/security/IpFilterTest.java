package com.example.sbjconvert.security;

import com.example.sbjconvert.model.GeoLocationResponse;
import com.example.sbjconvert.service.GeoLocationConfiguration;
import com.example.sbjconvert.service.GeoLocationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class IpFilterTest {

    @Mock
    private GeoLocationService geoLocationService;

    @Mock
    private GeoLocationConfiguration geoLocationConfiguration;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private IpFilter ipFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ipFilter = new IpFilter(geoLocationService, geoLocationConfiguration);
    }

    @Test
    void shouldNotCheckWhenGeolocationIsDisabled() throws Exception {
        when(geoLocationConfiguration.isEnabled()).thenReturn(false);
        ipFilter.init(null);
        ipFilter.doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
        verifyNoInteractions(geoLocationService);
    }

    @Test
    public void testDoFilter() throws Exception {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(geoLocationConfiguration.isEnabled()).thenReturn(true);
        when(geoLocationConfiguration.getAllowedIps()).thenReturn(Collections.emptyList());
        when(geoLocationConfiguration.getBlockedCountries()).thenReturn(Collections.emptyList());
        when(geoLocationConfiguration.getBlockedProviders()).thenReturn(Collections.emptyList());

        GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
        geoLocationResponse.setStatus("success");
        when(geoLocationService.getDetails(anyString())).thenReturn(geoLocationResponse);

        ipFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testWhenIpIsBlocked() throws Exception {
        GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
        geoLocationResponse.setStatus("success");
        when(geoLocationService.getDetails(anyString())).thenReturn(geoLocationResponse);
        when(geoLocationConfiguration.getAllowedIps()).thenReturn(Collections.emptyList());
        ipFilter.doFilter(request, response, chain);
        verify(response, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }

    @Test
    void shouldAllowWhenIpIsInAllowedIps() {
        when(geoLocationConfiguration.getAllowedIps()).thenReturn(List.of("127.0.0.1"));
        assertFalse(ipFilter.isBlocked("127.0.0.1"));
    }

    @Test
    public void shouldBlockWhenIpIsNotInAllowedIps() {
        when(geoLocationConfiguration.getAllowedIps()).thenReturn(Collections.emptyList());
        assertTrue(ipFilter.isBlocked("127.0.0.1"));
    }

    @Test
    public void shouldBlockProvider() {
        GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
        geoLocationResponse.setIsp("Comcast");

        when(geoLocationConfiguration.getBlockedProviders()).thenReturn(Collections.emptyList());
        assertFalse(ipFilter.isProviderBlocked(geoLocationResponse));

        when(geoLocationConfiguration.getBlockedProviders()).thenReturn(List.of("Comcast"));
        assertTrue(ipFilter.isProviderBlocked(geoLocationResponse));
    }

    @Test
    public void shouldBlockCountry() {
        GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
        geoLocationResponse.setCountryCode("US");

        when(geoLocationConfiguration.getBlockedCountries()).thenReturn(Collections.emptyList());
        assertFalse(ipFilter.isCountryBlocked(geoLocationResponse));

        when(geoLocationConfiguration.getBlockedCountries()).thenReturn(List.of("US"));
        assertTrue(ipFilter.isCountryBlocked(geoLocationResponse));
    }
}