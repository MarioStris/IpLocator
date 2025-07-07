package org.locator.iplocator.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseIpLocation {


    @JsonAlias({"continent_name", "continentName", "continent"})
    private String continent;

    @JsonAlias({"country", "country_name", "countryName"})
    private String countryName;

    @JsonAlias({"regionName", "region", "state_prov", "state", "region_name"})
    private String regionName;

    @JsonAlias({"city", "cityName", "city_name"})
    private String cityName;

    @JsonAlias({"lat", "latitude"})
    private Double latitude;

    @JsonAlias({"lon", "lng", "longitude"})
    private Double longitude;

}
