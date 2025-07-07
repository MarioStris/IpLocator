package org.locator.iplocator;

import org.junit.jupiter.api.extension.ExtendWith;
import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.viewModel.IpProvider;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TestHelper {

    public IpProvider createIpProvider(String shortCode, String name) {
        IpProvider provider = new IpProvider();
        provider.setName(name);
        provider.setShortCode(shortCode);
        return provider;
    }

    public BaseIpLocation createMockBaseIpLocation() {
        BaseIpLocation location = new BaseIpLocation();
        location.setContinent("United States");
        location.setCountryName("United States");
        location.setCityName("Mountain View");
        location.setRegionName("California");
        location.setLatitude(37.386);
        location.setLongitude(-122.0838);
        return location;
    }
}