package org.locator.iplocator.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locator.iplocator.model.BaseIpLocation;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpLocationResponse extends BaseIpLocation {


    @JsonAlias({"ip_version", "version"})
    private Integer ipVersion;

    @JsonAlias({"ip", "query", "ip_address"})
    private String ipAddress;

    @JsonAlias({"country_code", "countryCode", "country_code2"})
    private String countryCode;

    @JsonAlias({"capital_city", "capitalCity"})
    private String capital;

    @JsonAlias({"phone_codes", "phoneCodes", "calling_codes"})
    private List<Integer> phoneCodes;

    @JsonAlias({"time_zones", "timeZones", "timezone"})
    private List<String> timeZones;

    @JsonAlias({"continent_code", "continentCode"})
    private String continentCode;

    @JsonAlias({"as", "autonomous_system"})
    private String asn;

    @JsonAlias({"as_org", "asn_organization", "org"})
    private String asnOrganization;
}
