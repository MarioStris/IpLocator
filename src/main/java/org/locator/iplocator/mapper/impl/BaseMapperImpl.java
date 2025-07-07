package org.locator.iplocator.mapper.impl;

import org.locator.iplocator.mapper.BaseMapper;
import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.dto.IpLocationResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BaseMapperImpl implements BaseMapper {

    @Override
    public BaseIpLocation map(IpLocationResponse response) {
        BaseIpLocation base = new BaseIpLocation();
        BeanUtils.copyProperties(response, base);
        return base;
    }
}
