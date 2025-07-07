package org.locator.iplocator.facade;

import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.viewModel.IpProvider;

import java.util.List;

public interface IpLocationFacade {
    List<IpProvider> getAvailableProviders();

    BaseIpLocation getIpLocationByProvider(String ip, String provider);
}
