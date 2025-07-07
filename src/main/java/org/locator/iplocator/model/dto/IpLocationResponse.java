package org.locator.iplocator.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locator.iplocator.model.BaseIpLocation;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class IpLocationResponse extends BaseIpLocation {

    private Integer ipVersion;
    private String ipAddress;
    private String countryCode;
    private String capital;
    private List<Integer> phoneCodes;
    private List<String> timeZones;
    private String continentCode;
    private List<String> currencies;
    private List<String> languages;
    private String asn;
    private String asnOrganization;

}
