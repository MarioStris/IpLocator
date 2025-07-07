package org.locator.iplocator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locator.iplocator.validation.ValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ProvidersEnum {

    IPAPI("IP-API", "IPAPI", Status.ACTIVE, "${iplocator.providers.ipapi.url}"),
    IPSTACK("IPStack", "IPSTACK", Status.INACTIVE, "${iplocator.providers.ipstack.url}"),
    IPGEOLOCATION("IP Geolocation", "IPGEO", Status.INACTIVE, "${iplocator.providers.ipgeolocation.url}"),
    IPINFO("IPInfo", "IPINFO", Status.INACTIVE, "${iplocator.providers.ipinfo.url}"),
    MAXMIND("MaxMind", "MAXMIND", Status.INACTIVE, "${iplocator.providers.maxmind.url}"),
    IPDATA("IPData", "IPDATA", Status.INACTIVE, "${iplocator.providers.ipdata.url}"),
    IPREGISTRY("IP Registry", "IPREG", Status.INACTIVE, "${iplocator.providers.ipregistry.url}");

    private final String name;
    private final String shortCode;
    private final Status status;
    private final String url;

    public enum Status {
        ACTIVE, INACTIVE, MAINTENANCE
    }

    private static final List<ProvidersEnum> ACTIVE_PROVIDERS = Arrays.stream(ProvidersEnum.values())
            .filter(provider -> provider.getStatus() == Status.ACTIVE)
            .collect(Collectors.toList());

    public static List<ProvidersEnum> getActiveProviders() {
        return ACTIVE_PROVIDERS;
    }

    public static ProvidersEnum getByShortCode(String shortCode) {
        for (ProvidersEnum e : values()) {
            if (e.shortCode.equals(shortCode)) return e;
        }

        throw new ValidationException("Provider with short code '" + shortCode + "' not found.");
    }
}
