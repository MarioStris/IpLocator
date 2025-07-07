package org.locator.iplocator.facade.impl;

import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.facade.IpLocationFacade;
import org.locator.iplocator.mapper.BaseMapper;
import org.locator.iplocator.mapper.IpProviderMapper;
import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.dto.IpLocationResponse;
import org.locator.iplocator.model.viewModel.IpProvider;
import org.locator.iplocator.services.IpLocatorService;
import org.locator.iplocator.validator.IpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IpLocationFacadeImpl implements IpLocationFacade {

    @Autowired
    private IpLocatorService service;
    @Autowired
    private IpProviderMapper providerMapper;
    @Autowired
    private IpValidator validator;
    @Autowired
    private BaseMapper baseMapper;

    @Override
    public List<IpProvider> getAvailableProviders() {
        return service.getProviders().stream().map(providerMapper::map).collect(Collectors.toList());
    }

    @Override
    public BaseIpLocation getIpLocationByProvider(String ip, String provider) {
        //validate IP address
        validator.validateIp(ip);
        //validate provider
        validator.validateProvider(provider);
        //API provider
        ProvidersEnum providersEnum = ProvidersEnum.getByShortCode(provider);
        //call service to get IP location info
        IpLocationResponse response = service.getIpLocationInfo(providersEnum, ip);

        return baseMapper.map(response);
    }
}
