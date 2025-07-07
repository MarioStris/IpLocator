package org.locator.iplocator.services.impl;

import org.locator.iplocator.client.IpInfoClient;
import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.model.dto.IpLocationResponse;
import org.locator.iplocator.services.IpLocatorService;
import org.locator.iplocator.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IpLocatorServiceImpl implements IpLocatorService {

    @Autowired
    private IpInfoClient client;

    @Override
    public List<ProvidersEnum> getProviders() {
        return Optional.of(ProvidersEnum.getActiveProviders())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ValidationException("No active providers found"));
    }

    @Override
    @Cacheable(value = "ipLocationCache", key = "#providersEnum.shortCode + ':' + #ip")
    public IpLocationResponse getIpLocationInfo(ProvidersEnum providersEnum, String ip) {
        return client.getIpLocationInfo(providersEnum, ip);
    }
}
