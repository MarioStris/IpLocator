package org.locator.iplocator.services;

import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.model.dto.IpLocationResponse;

import java.util.List;

public interface IpLocatorService {
    List<ProvidersEnum> getProviders();

    IpLocationResponse getIpLocationInfo(ProvidersEnum providersEnum, String id);
}
