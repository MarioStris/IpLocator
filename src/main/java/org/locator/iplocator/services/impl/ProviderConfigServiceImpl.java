package org.locator.iplocator.services.impl;

import lombok.RequiredArgsConstructor;
import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.services.ProviderConfigService;
import org.locator.iplocator.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProviderConfigServiceImpl implements ProviderConfigService {

    private final Environment environment;

    @Value("${iplocator.providers.ipgeolocation.apikey}")
    private String ipGeolocationApiKey;

    @Override
    public String getResolvedUrl(ProvidersEnum provider, String ip) {
        String baseUrl = environment.getProperty(getPropertyKey(provider));
        if (baseUrl == null) {
            throw new ValidationException("URL not configured for provider: " + provider.getShortCode());
        }

        if (provider == ProvidersEnum.IPGEOLOCATION) {
            if (Objects.isNull(ipGeolocationApiKey)) {
                throw new ValidationException("API key not configured for IPGEOLOCATION provider");
            }

            return UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("apiKey", ipGeolocationApiKey)
                    .queryParam("ip", ip)
                    .toUriString();
        }

        return UriComponentsBuilder.fromHttpUrl(baseUrl.replace("{ip}", ip))
                .toUriString();
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
