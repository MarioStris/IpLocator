package org.locator.iplocator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locator.iplocator.controllers.IpLocatorController;
import org.locator.iplocator.facade.IpLocationFacade;
import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.viewModel.IpProvider;
import org.locator.iplocator.validation.ValidationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IpLocatorControllerTest {

    @Mock
    private IpLocationFacade ipLocationFacade;

    @InjectMocks
    private IpLocatorController ipLocatorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ipLocatorController).build();
        objectMapper = new ObjectMapper();
    }

    // Basic functionality tests
    @Test
    void getAvailableProviders_ShouldReturnProviderList_WhenCalled() throws Exception {
        // Given
        List<IpProvider> mockProviders = Arrays.asList(
                createIpProvider("IPAPI", "IP API"),
                createIpProvider("IPSTACK", "IP Stack"),
                createIpProvider("IPINFO", "IP Info")
        );
        when(ipLocationFacade.getAvailableProviders()).thenReturn(mockProviders);

        // When & Then
        mockMvc.perform(get("/api/ip-location/providers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].shortCode").value("IPAPI"))
                .andExpect(jsonPath("$[0].name").value("IP API"));

        verify(ipLocationFacade, times(1)).getAvailableProviders();
    }

    @Test
    void getIpLocationByProvider_ShouldReturnIpLocation_WhenValidParameters() throws Exception {
        // Given
        String ip = "8.8.8.8";
        String provider = "IPAPI";
        BaseIpLocation mockLocation = createMockBaseIpLocation();
        when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(mockLocation);

        // When & Then
        mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ip))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.continent").value("United States"))
                .andExpect(jsonPath("$.countryName").value("United States"));

        verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
    }

    @Test
    void getIpLocationByProvider_ShouldHandleMultipleRequests_WithinRateLimit() throws Exception {
        // Given
        String ip = "8.8.8.8";
        String provider = "IPAPI";
        BaseIpLocation mockLocation = createMockBaseIpLocation();
        when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(mockLocation);

        // When - Simulate multiple requests within rate limit
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ip))
                    .andExpect(status().isOk());
        }

        // Then
        verify(ipLocationFacade, times(5)).getIpLocationByProvider(ip, provider);
    }

    // Edge cases and validation tests
    @Test
    void getAvailableProviders_ShouldReturnEmptyList_WhenNoProvidersAvailable() throws Exception {
        // Given
        when(ipLocationFacade.getAvailableProviders()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/ip-location/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }




    @Test
    void getIpLocationByProvider_ShouldHandleIPv6Addresses() throws Exception {
        // Given
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String provider = "IPINFO";
        BaseIpLocation mockLocation = createCustomBaseIpLocation(ipv6, "United States", "Mountain View");
        when(ipLocationFacade.getIpLocationByProvider(ipv6, provider)).thenReturn(mockLocation);

        // When & Then
        mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ipv6))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryName").value("United States"));
    }

    @Test
    void getIpLocationByProvider_ShouldHandlePrivateIPAddresses() throws Exception {
        // Given
        String privateIp = "192.168.1.1";
        String provider = "IPAPI";
        BaseIpLocation mockLocation = createCustomBaseIpLocation(privateIp, "Private Network", "Local");
        when(ipLocationFacade.getIpLocationByProvider(privateIp, provider)).thenReturn(mockLocation);

        // When & Then
        mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, privateIp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryName").value("Private Network"));
    }

    // Concurrent access tests
    @Test
    void getIpLocationByProvider_ShouldHandleConcurrentRequests() throws Exception {
        // Given
        String ip = "8.8.8.8";
        String provider = "IPAPI";
        BaseIpLocation mockLocation = createMockBaseIpLocation();
        when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(mockLocation);

        // When - Simulate concurrent requests
        Thread thread1 = new Thread(() -> {
            try {
                mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ip))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                // Handle exception
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ip))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                // Handle exception
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Then - Both requests should be processed
        verify(ipLocationFacade, atLeast(2)).getIpLocationByProvider(ip, provider);
    }

    // Provider-specific tests
    @Test
    void getIpLocationByProvider_ShouldWorkWithDifferentProviders() throws Exception {
        // Given
        String ip = "1.1.1.1";
        String[] providers = {"IPAPI", "IPSTACK", "IPINFO", "MAXMIND"};
        BaseIpLocation mockLocation = createMockBaseIpLocation();

        for (String provider : providers) {
            when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(mockLocation);
        }

        // When & Then
        for (String provider : providers) {
            mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ip))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.countryName").value("United States"));

            verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
        }
    }



    // Null handling tests
    @Test
    void getIpLocationByProvider_ShouldHandleNullResponse_FromFacade() throws Exception {
        // Given
        String ip = "8.8.8.8";
        String provider = "IPAPI";
        when(ipLocationFacade.getIpLocationByProvider(ip, provider)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ip))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void getAvailableProviders_ShouldHandleNullResponse_FromFacade() throws Exception {
        // Given
        when(ipLocationFacade.getAvailableProviders()).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/ip-location/providers"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    // Parameter order verification
    @Test
    void getIpLocationByProvider_ShouldCallFacadeWithCorrectParameterOrder() throws Exception {
        // Given
        String ip = "203.0.113.1";
        String provider = "MAXMIND";
        BaseIpLocation mockLocation = createMockBaseIpLocation();
        when(ipLocationFacade.getIpLocationByProvider(anyString(), anyString())).thenReturn(mockLocation);

        // When
        mockMvc.perform(get("/api/ip-location/lookup/provider/{provider}/ip/{ip}", provider, ip))
                .andExpect(status().isOk());

        // Then - Verify correct parameter order (ip first, then provider)
        verify(ipLocationFacade, times(1)).getIpLocationByProvider(ip, provider);
        verify(ipLocationFacade, never()).getIpLocationByProvider(provider, ip);
    }

    // Helper methods
    private IpProvider createIpProvider(String shortCode, String name) {
        IpProvider provider = new IpProvider();
        provider.setName(name);
        provider.setShortCode(shortCode);
        return provider;
    }

    private BaseIpLocation createMockBaseIpLocation() {
        BaseIpLocation location = new BaseIpLocation();
        location.setContinent("United States");
        location.setCountryName("United States");
        location.setCityName("Mountain View");
        location.setRegionName("California");
        location.setLatitude(37.386);
        location.setLongitude(-122.0838);
        return location;
    }

    private BaseIpLocation createCustomBaseIpLocation(String ip, String country, String city) {
        BaseIpLocation location = new BaseIpLocation();
        location.setContinent(country);
        location.setCountryName(country);
        location.setCityName(city);
        location.setLatitude(37.386);
        location.setLongitude(-122.0838);
        return location;
    }
}