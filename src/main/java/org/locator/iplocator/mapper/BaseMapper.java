package org.locator.iplocator.mapper;

import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.dto.IpLocationResponse;

public interface BaseMapper {
    BaseIpLocation map (IpLocationResponse response);
}
