package com.bionic.fp.domain;

import com.bionic.fp.Constants.RestConstants.GEO;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

/**
 * Geographical coordinate
 *
 * @author Sergiy Gabriel
 */
@Embeddable
public class Coordinate {

    /**
     * Latitude describes how far north or south of the equator a point is located.
     * Points along the equator have latitudes of zero. Latitudes are values in the range [-90, 90]
     */
    @Column(name = "lat")
    @JsonProperty(GEO.LATITUDE)
    private double latitude;
    /**
     * Longitude describes how far east a point is, from the prime meridian
     * Longitudes are values in the range [-180, 180]
     */
    @Column(name = "lng")
    @JsonProperty(GEO.LONGITUDE)
    private double longitude;

    public Coordinate() {
    }

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Objects.equals(this.latitude, that.latitude) &&
                Objects.equals(this.longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("lat=").append(latitude);
        sb.append(", lng=").append(longitude);
        sb.append('}');
        return sb.toString();
    }
}
