package org.locator.iplocator.services;

import org.locator.iplocator.enums.ProvidersEnum;

public interface ProviderConfigService {
    String getResolvedUrl(ProvidersEnum provider, String ip);
}
