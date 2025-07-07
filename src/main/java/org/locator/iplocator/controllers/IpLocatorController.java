package org.locator.iplocator.controllers;

import lombok.extern.slf4j.Slf4j;
import org.locator.iplocator.facade.IpLocationFacade;
import org.locator.iplocator.model.BaseIpLocation;
import org.locator.iplocator.model.viewModel.IpProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ip-location")
public class IpLocatorController {

    @Autowired
    IpLocationFacade ipLocationFacade;

    @GetMapping("/providers")
    public ResponseEntity<List<IpProvider>> getAvailableProviders() {
        log.info("Getting available IP location providers");

        List<IpProvider> providerList = ipLocationFacade.getAvailableProviders();

        return ResponseEntity.ok(providerList);
    }

    @GetMapping("/lookup/provider/{provider}/ip/{ip}")
    public ResponseEntity<BaseIpLocation> getIpLocationByProvider(
            @PathVariable String ip,
            @PathVariable String provider) {
        log.info("Getting location for IP: {} using provider: {}", ip, provider);
        BaseIpLocation response = ipLocationFacade.getIpLocationByProvider(ip, provider);
        return ResponseEntity.ok(response);
    }

}
