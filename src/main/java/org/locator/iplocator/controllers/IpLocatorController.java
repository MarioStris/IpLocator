package org.locator.iplocator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "IP Locator", description = "IP geolocation operations")
public class IpLocatorController {

    @Autowired
    IpLocationFacade ipLocationFacade;

    @GetMapping("/providers")
    @Operation(
            summary = "Get available providers",
            description = "Returns list of available IP location providers"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved providers",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IpProvider.class),
                    examples = @ExampleObject(
                            name = "Provider List Example",
                            value = "[{\"name\": \"ipapi\", \"active\": true}, {\"name\": \"ipstack\", \"active\": true}]"
                    )
            )
    )
    public ResponseEntity<List<IpProvider>> getAvailableProviders() {
        log.info("Getting available IP location providers");

        List<IpProvider> providerList = ipLocationFacade.getAvailableProviders();

        return ResponseEntity.ok(providerList);
    }

    @GetMapping("/lookup/provider/{provider}/ip/{ip}")
    @Operation(
            summary = "Get IP location by provider",
            description = "Returns IP location information from specified provider"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved IP location",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaseIpLocation.class),
                    examples = @ExampleObject(
                            name = "IP Location Example",
                            value = "{\"ip\": \"8.8.8.8\", \"country\": \"United States\", \"city\": \"Mountain View\", \"latitude\": 37.4056, \"longitude\": -122.0775}"
                    )
            )
    )
    public ResponseEntity<BaseIpLocation> getIpLocationByProvider(
            @Parameter(description = "IP address to lookup", example = "8.8.8.8") @PathVariable String ip,
            @Parameter(description = "Provider name", example = "ipapi") @PathVariable String provider) {
        log.info("Getting location for IP: {} using provider: {}", ip, provider);
        BaseIpLocation response = ipLocationFacade.getIpLocationByProvider(ip, provider);
        return ResponseEntity.ok(response);
    }

}
