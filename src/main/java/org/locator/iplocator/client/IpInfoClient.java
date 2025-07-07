package org.locator.iplocator.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.model.dto.IpLocationResponse;
import org.locator.iplocator.services.ProviderConfigService;
import org.locator.iplocator.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class IpInfoClient {

    private final RestTemplate restTemplate;

    private final ProviderConfigService providerConfigService;

    public IpLocationResponse getIpLocationInfo(ProvidersEnum providersEnum, String ip) {
        String resolvedUrl = providerConfigService.getResolvedUrl(providersEnum, ip);

        try {
            log.info("Making API call: {}", resolvedUrl);
            IpLocationResponse response = restTemplate.getForObject(resolvedUrl, IpLocationResponse.class);
            return response;
        } catch (RestClientException exception) {
            log.error("error getting IP info", exception);
            throw new ValidationException("Error getting IP info: " + exception.getMessage());
        }
    }
}
