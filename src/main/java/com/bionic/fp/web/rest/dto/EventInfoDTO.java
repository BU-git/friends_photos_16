package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Event;
import com.bionic.fp.util.LocalDateTimeJsonDeserializer;
import com.bionic.fp.util.LocalDateTimeJsonSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.ArrayUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

/**
 * Holds group info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventInfoDTO {

    @JsonProperty("event_id")
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
    @JsonProperty("type_id")
    private Integer typeId;
    @JsonProperty("owner_id")
    private Long ownerId;
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lng")
    private Double longitude;
    private Float radius;
    private Boolean geo;
    private Boolean visible;
    @JsonProperty("private")
    private Boolean isPrivate;

    public EventInfoDTO() {
    }

    public EventInfoDTO(final Event event) {
        build(event);
    }

    public void build(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.date = event.getDate();
        this.expireDate = event.getExpireDate();
        this.typeId = event.getEventType().getId();
        this.ownerId = event.getOwner().getId();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.radius = event.getRadius();
        this.geo = event.isGeoServicesEnabled();
        this.visible = event.isVisible();
        this.isPrivate = event.isPrivate();
    }

    public Consumer<Event> getConsumer(final String fields) {
        Consumer<Event> result = addConsumer(fields, "event_id", null, e -> this.setId(e.getId()));
        result = addConsumer(fields, "name", result, e -> this.setName(e.getName()));
        result = addConsumer(fields, "description", result, e -> this.setDescription(e.getDescription()));
        result = addConsumer(fields, "date", result, e -> this.setDate(e.getDate()));
        result = addConsumer(fields, "expire_date", result, e -> this.setExpireDate(e.getExpireDate()));
        result = addConsumer(fields, "type_id", result, e -> this.setTypeId(e.getEventType().getId()));
        result = addConsumer(fields, "owner_id", result, e -> this.setOwnerId(e.getOwner().getId()));
        result = addConsumer(fields, "lat", result, e -> this.setLatitude(e.getLatitude()));
        result = addConsumer(fields, "lng", result, e -> this.setLongitude(e.getLongitude()));
        result = addConsumer(fields, "radius", result, e -> this.setRadius(e.getRadius()));
        result = addConsumer(fields, "geo", result, e -> this.setGeo(e.isGeoServicesEnabled()));
        result = addConsumer(fields, "visible", result, e -> this.setVisible(e.isVisible()));
        result = addConsumer(fields, "private", result, e -> this.setIsPrivate(e.isPrivate()));

        return result;
    }

    public Consumer<Event> getConsumer(final String... fields) {
        if(ArrayUtils.isNotEmpty(fields)) {
            List<String> list = asList(fields);

            Consumer<Event> result = addConsumer(list, "event_id", null, e -> this.setId(e.getId()));
            result = addConsumer(list, "name", result, e -> this.setName(e.getName()));
            result = addConsumer(list, "description", result, e -> this.setDescription(e.getDescription()));
            result = addConsumer(list, "date", result, e -> this.setDate(e.getDate()));
            result = addConsumer(list, "expire_date", result, e -> this.setExpireDate(e.getExpireDate()));
            result = addConsumer(list, "type_id", result, e -> this.setTypeId(e.getEventType().getId()));
            result = addConsumer(list, "owner_id", result, e -> this.setOwnerId(e.getOwner().getId()));
            result = addConsumer(list, "lat", result, e -> this.setLatitude(e.getLatitude()));
            result = addConsumer(list, "lng", result, e -> this.setLongitude(e.getLongitude()));
            result = addConsumer(list, "radius", result, e -> this.setRadius(e.getRadius()));
            result = addConsumer(list, "geo", result, e -> this.setGeo(e.isGeoServicesEnabled()));
            result = addConsumer(list, "visible", result, e -> this.setVisible(e.isVisible()));
            result = addConsumer(list, "private", result, e -> this.setIsPrivate(e.isPrivate()));

            return result;
        }
        return null;
    }

    private Consumer<Event> addConsumer(final String fields, final String field,
                                        final Consumer<Event> result, final Consumer<Event> consumer) {
        if(fields.matches("(.*[,\\s]+?|^)" + field +"([,\\s].*|$)")) {
            return result == null ? consumer : result.andThen(consumer);
        }
        return result;
    }

    private Consumer<Event> addConsumer(final List<String> fields, final String field,
                                        final Consumer<Event> result, final Consumer<Event> consumer) {
        if(fields.stream().parallel().filter(s -> s.equalsIgnoreCase(field)).findAny().isPresent()) {
            return result == null ? consumer : result.andThen(consumer);
        }
        return result;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Boolean getGeo() {
        return geo;
    }

    public void setGeo(Boolean geo) {
        this.geo = geo;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
