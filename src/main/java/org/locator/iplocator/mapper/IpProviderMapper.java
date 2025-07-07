package org.locator.iplocator.mapper;

import org.locator.iplocator.enums.ProvidersEnum;
import org.locator.iplocator.model.viewModel.IpProvider;

public interface IpProviderMapper {
    IpProvider map(ProvidersEnum providers);
}
