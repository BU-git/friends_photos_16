package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Coordinate;
import com.bionic.fp.domain.Event;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bionic.fp.Constants.RestConstants.*;

/**
 * Holds event for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class EventInput {

    @JsonProperty(EVENT.NAME)           private String name;
    @JsonProperty(EVENT.DESCRIPTION)    private String description;
    @JsonProperty(EVENT.TYPE_ID)        private Long eventTypeId;
    @JsonProperty(EVENT.LOCATION)       private Coordinate location;
    @JsonProperty(EVENT.GEO)            private Boolean geo;
    @JsonProperty(EVENT.VISIBLE)        private Boolean visible;
    @JsonProperty(EVENT.PRIVATE)        private Boolean isPrivate;
    @JsonProperty(EVENT.PASSWORD)       private String password;

    public EventInput() {
    }

    public EventInput(final Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.eventTypeId = event.getEventType().getId();
        this.visible = event.isVisible();
        this.location = event.getLocation();
        this.geo = event.isGeoServicesEnabled();
        this.isPrivate = event.isPrivate();
        this.password = event.getPassword();
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
    public Long getEventTypeId() {
        return eventTypeId;
    }
    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
    public Boolean getVisible() {
        return visible;
    }
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    public Coordinate getLocation() {
        return location;
    }
    public void setLocation(Coordinate location) {
        this.location = location;
    }
    public Boolean getGeo() {
        return geo;
    }
    public void setGeo(Boolean geo) {
        this.geo = geo;
    }
    public Boolean getPrivate() {
        return isPrivate;
    }
    public void setPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
