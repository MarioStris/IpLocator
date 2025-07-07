package org.locator.iplocator.controllers;

import lombok.extern.slf4j.Slf4j;
import org.locator.iplocator.enums.ProvidersEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/ip-location")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @DeleteMapping("/cache/clear")
    @CacheEvict(value = "ipLocationCache", allEntries = true)
    public ResponseEntity<String> clearCache() {
        log.info("Clearing IP location cache");
        return ResponseEntity.ok("Cache cleared successfully");
    }

    @DeleteMapping("/cache/clear/ip/{ip}")
    public ResponseEntity<String> clearCacheForIp(@PathVariable String ip) {

        log.info("Clearing cache for IP: {}", ip);
        ProvidersEnum.getActiveProviders().forEach(provider ->
                cacheManager.getCache("ipLocationCache")
                        .evict(provider.getShortCode() + ":" + ip)
        );
        log.info("Cache cleared for IP: {}", ip);

        return ResponseEntity.ok("Cache cleared for IP: " + ip);
    }
}
