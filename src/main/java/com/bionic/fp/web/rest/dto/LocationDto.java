package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.EVENT;
import com.bionic.fp.Constants.RestConstants.GEO;
import com.bionic.fp.domain.Coordinate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds geo info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationDto {

    @JsonProperty(EVENT.LOCATION)   private Coordinate location;
    @JsonProperty(GEO.RADIUS)       private Float radius;
    @JsonProperty(GEO.SW)           private Coordinate sw;
    @JsonProperty(GEO.NE)           private Coordinate ne;

    public LocationDto() {
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Coordinate getSw() {
        return sw;
    }

    public void setSw(Coordinate sw) {
        this.sw = sw;
    }

    public Coordinate getNe() {
        return ne;
    }

    public void setNe(Coordinate ne) {
        this.ne = ne;
    }
}
