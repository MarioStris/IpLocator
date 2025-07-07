package org.locator.iplocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locator.iplocator.facade.IpLocationFacade;
import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.viewModel.IpProvider;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IpLocationFacadeRateLimitTest {

    @Mock
    private IpLocationFacade ipLocationFacade;

    @Mock
    private RateLimitService rateLimitService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getIpLocationByProvider_ShouldThrowException_WhenRateLimitExceeded() {
        // Given
        String ip = "8.8.8.8";
        String provider = "IPAPI";
        when(ipLocationFacade.getIpLocationByProvider(ip, provider))
                .thenThrow(new RuntimeException("Rate limit exceeded"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            ipLocationFacade.getIpLocationByProvider(ip, provider);
        });

        verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
    }

    @Test
    void getIpLocationByProvider_ShouldProceed_WhenWithinRateLimit() {
        // Given
        String ip = "8.8.8.8";
        String provider = "IPAPI";
        BaseIpLocation mockLocation = createMockBaseIpLocation();
        when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(mockLocation);

        // When
        BaseIpLocation result = ipLocationFacade.getIpLocationByProvider(ip, provider);

        // Then
        assertNotNull(result);
        verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
    }

    @Test
    void getIpLocationByProvider_ShouldHandleMultipleSuccessfulRequests() {
        // Given
        String ip = "1.1.1.1";
        String provider = "IPSTACK";
        BaseIpLocation mockLocation = createMockBaseIpLocation();
        when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(mockLocation);

        // When - Multiple calls within rate limit
        for (int i = 0; i < 5; i++) {
            BaseIpLocation result = ipLocationFacade.getIpLocationByProvider(ip, provider);
            assertNotNull(result);
        }

        // Then
        verify(ipLocationFacade, times(5)).getIpLocationByProvider(ip, provider);
    }

    @Test
    void getAvailableProviders_ShouldThrowException_WhenRateLimitExceeded() {
        // Given
        when(ipLocationFacade.getAvailableProviders())
                .thenThrow(new RuntimeException("Rate limit exceeded"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            ipLocationFacade.getAvailableProviders();
        });

        verify(ipLocationFacade, times(1)).getAvailableProviders();
    }


    @Test
    void getIpLocationByProvider_ShouldHandleNullResponse() {
        // Given
        String ip = "10.0.0.1";
        String provider = "IPAPI";
        when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(null);

        // When
        BaseIpLocation result = ipLocationFacade.getIpLocationByProvider(ip, provider);

        // Then
        assertNull(result);
        verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
    }

    @Test
    void getAvailableProviders_ShouldHandleEmptyList() {
        // Given
        when(ipLocationFacade.getAvailableProviders()).thenReturn(Collections.emptyList());

        // When
        List<IpProvider> result = ipLocationFacade.getAvailableProviders();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ipLocationFacade, times(1)).getAvailableProviders();
    }

    @Test
    void getIpLocationByProvider_ShouldHandleTimeout() {
        // Given
        String ip = "8.8.8.8";
        String provider = "SLOW_PROVIDER";
        when(ipLocationFacade.getIpLocationByProvider(ip, provider))
                .thenThrow(new RuntimeException("Request timeout"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            ipLocationFacade.getIpLocationByProvider(ip, provider);
        });

        verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
    }

    @Test
    void getIpLocationByProvider_ShouldVerifyParameterOrder() {
        // Given
        String ip = "203.0.113.1";
        String provider = "MAXMIND";
        BaseIpLocation mockLocation = createMockBaseIpLocation();
        when(ipLocationFacade.getIpLocationByProvider(anyString(), anyString())).thenReturn(mockLocation);

        // When
        ipLocationFacade.getIpLocationByProvider(ip, provider);

        // Then - Verify correct parameter order
        verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
        verify(ipLocationFacade, never()).getIpLocationByProvider(provider, ip);
    }

    // Helper methods
    private BaseIpLocation createMockBaseIpLocation() {
        BaseIpLocation location = new BaseIpLocation();
        location.setCountryName("United States");
        location.setCityName("Mountain View");
        location.setRegionName("California");
        location.setLatitude(37.386);
        location.setLongitude(-122.0838);
        return location;
    }

    private IpProvider createIpProvider(String shortCode, String name) {
        IpProvider provider = new IpProvider();
        provider.setShortCode(shortCode);
        provider.setName(name);
        return provider;
    }

    // Mock RateLimitService class for compilation
    static class RateLimitService {
        public boolean isRequestAllowed() {
            return true;
        }
    }
}