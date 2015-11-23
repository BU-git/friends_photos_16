package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.util.LocalDateTimeJsonDeserializer;
import com.bionic.fp.util.LocalDateTimeJsonSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * Holds group info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventInfoDTO {

    private Long id;
    private String name;
    private String description;
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    private LocalDateTime date;
    @JsonProperty("expire_date")
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    private LocalDateTime expireDate;
    private Integer typeId;
    private OwnerInfoDTO owner;
    private Double latitude;
    private Double longitude;
    private Float radius;
    private Boolean geolocation;
    private Boolean visible;

    public EventInfoDTO() {
    }

    public EventInfoDTO(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.date = event.getDate();
        this.expireDate = event.getExpireDate();
        this.typeId = event.getEventType().getId();
        this.owner = new OwnerInfoDTO(event.getOwner());
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.radius = event.getRadius();
        this.geolocation = event.isGeolocationServicesEnabled();
        this.visible = event.isVisible();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public OwnerInfoDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerInfoDTO owner) {
        this.owner = owner;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Boolean getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Boolean geolocation) {
        this.geolocation = geolocation;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
