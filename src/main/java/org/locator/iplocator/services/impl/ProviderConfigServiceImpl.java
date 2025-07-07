package org.locator.iplocator.services.impl;

import lombok.RequiredArgsConstructor;
import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.services.ProviderConfigService;
import org.locator.iplocator.validation.ValidationException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderConfigServiceImpl implements ProviderConfigService {

    private final Environment environment;

    @Override
    public String getResolvedUrl(ProvidersEnum provider, String ip) {
        String baseUrl = environment.getProperty(getPropertyKey(provider));
        if (baseUrl == null) {
            throw new ValidationException("URL not configured for provider: " + provider.getShortCode());
        }
        return baseUrl.replace("{ip}", ip);
    }

    private String getPropertyKey(ProvidersEnum provider) {
        return switch (provider) {
            case IPAPI -> "iplocator.providers.ipapi.url";
            case IPSTACK -> "iplocator.providers.ipstack.url";
            case IPGEOLOCATION -> "iplocator.providers.ipgeolocation.url";
            case IPINFO -> "iplocator.providers.ipinfo.url";
            case MAXMIND -> "iplocator.providers.maxmind.url";
            case IPDATA -> "iplocator.providers.ipdata.url";
            case IPREGISTRY -> "iplocator.providers.ipregistry.url";
        };
    }
}
