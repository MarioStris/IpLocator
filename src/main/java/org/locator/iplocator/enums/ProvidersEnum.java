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

    IPAPI("IP-API", "IPAPI", Status.ACTIVE),
    IPSTACK("IPStack", "IPSTACK", Status.INACTIVE),
    IPGEOLOCATION("IP Geolocation", "IPGEO", Status.INACTIVE),
    IPINFO("IPInfo", "IPINFO", Status.INACTIVE),
    MAXMIND("MaxMind", "MAXMIND", Status.INACTIVE),
    IPDATA("IPData", "IPDATA", Status.INACTIVE),
    IPREGISTRY("IP Registry", "IPREG", Status.INACTIVE);

    private final String name;
    private final String shortCode;
    private final Status status;

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
