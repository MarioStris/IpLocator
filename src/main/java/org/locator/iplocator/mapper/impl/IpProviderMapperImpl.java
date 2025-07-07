package org.locator.iplocator.mapper.impl;

import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.mapper.IpProviderMapper;
import org.locator.iplocator.model.viewModel.IpProvider;
import org.springframework.stereotype.Component;

@Component
public class IpProviderMapperImpl implements IpProviderMapper {

    @Override
    public IpProvider map(ProvidersEnum providers) {
        IpProvider ipProvider = new IpProvider();

        ipProvider.setName(providers.getName());
        ipProvider.setShortCode(providers.getShortCode());

        return ipProvider;
    }
}
