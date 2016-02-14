package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.EVENT;
import com.bionic.fp.domain.Coordinate;
import com.bionic.fp.domain.Event;
import com.bionic.fp.util.LocalDateTimeJsonDeserializer;
import com.bionic.fp.util.LocalDateTimeJsonSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.bionic.fp.util.Checks.check;

/**
 * Holds event info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventInfo {

    @JsonProperty(EVENT.ID)                 private Long id;
    @JsonProperty(EVENT.NAME)               private String name;
    @JsonProperty(EVENT.DESCRIPTION)        private String description;
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonProperty(EVENT.DATE)               private LocalDateTime date;
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonProperty(EVENT.EXPIRE_DATE)        private LocalDateTime expireDate;
    @JsonProperty(EVENT.TYPE_ID)            private Long typeId;
    @JsonProperty(EVENT.LOCATION)           private Coordinate location;
    @JsonProperty(EVENT.GEO)                private Boolean geo;
    @JsonProperty(EVENT.VISIBLE)            private Boolean visible;
    @JsonProperty(EVENT.PRIVATE)            private Boolean isPrivate;

    public EventInfo() {
    }

    public EventInfo(final Event event) {
        build(event);
    }

    public void build(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.date = event.getCreated();
        this.expireDate = event.getExpireDate();
        this.typeId = event.getEventType().getId();
        this.location = event.getLocation();
        this.geo = event.isGeoServicesEnabled();
        this.visible = event.isVisible();
        this.isPrivate = event.isPrivate();
    }

    public static class Transformer {
        private final Event event;
        private final EventInfo dto;

        public Transformer(final Event event, final EventInfo dto) {
            check(event != null, "The event is not initialized");
            check(dto != null, "The event dto is not initialized");
            this.event = event;
            this.dto = dto;
        }

        public Event getEvent() {
            return this.event;
        }

        public EventInfo getDto() {
            return this.dto;
        }

        public static EventInfo transform(final Event event, final String fields) {
            if(event == null) {
                return null;
            }
            EventInfo dto = new EventInfo();
            Consumer<Transformer> consumer = getConsumer(fields);
            if(consumer != null) {
                consumer.accept(new Transformer(event, dto));
                return dto;
            }
            dto.build(event);
            return dto;
        }

        public static List<EventInfo> transform(final List<Event> events, final String fields) {
            if(events == null || events.isEmpty()) {
                return Collections.emptyList();
            }
            Consumer<Transformer> consumer = getConsumer(fields);
            if(consumer != null) {
                return events.stream().parallel()
                        .map(event -> {
                            EventInfo dto = new EventInfo();
                            consumer.accept(new Transformer(event, dto));
                            return dto;
                        })
                        .collect(Collectors.toList());
            }
            return events.stream().parallel()
                    .map(EventInfo::new)
                    .collect(Collectors.toList());
        }

        public static Consumer<Transformer> getConsumer(final String fields) {
            if(StringUtils.isNotEmpty(fields)) {
                Consumer<Transformer> result = addConsumer(fields, EVENT.ID, null, t -> t.getDto().setId(t.getEvent().getId()));
                result = addConsumer(fields, EVENT.NAME, result, t -> t.getDto().setName(t.getEvent().getName()));
                result = addConsumer(fields, EVENT.DESCRIPTION, result, t -> t.getDto().setDescription(t.getEvent().getDescription()));
                result = addConsumer(fields, EVENT.DATE, result, t -> t.getDto().setDate(t.getEvent().getCreated()));
                result = addConsumer(fields, EVENT.EXPIRE_DATE, result, t -> t.getDto().setExpireDate(t.getEvent().getExpireDate()));
                result = addConsumer(fields, EVENT.TYPE_ID, result, t -> t.getDto().setTypeId(t.getEvent().getEventType().getId()));
                result = addConsumer(fields, EVENT.LOCATION, result, t -> {
                    if(t.getEvent().isGeoServicesEnabled()) {
                        t.getDto().setLocation(t.getEvent().getLocation());
                    }
                });
                result = addConsumer(fields, EVENT.GEO, result, t -> t.getDto().setGeo(t.getEvent().isGeoServicesEnabled()));
                result = addConsumer(fields, EVENT.VISIBLE, result, t -> t.getDto().setVisible(t.getEvent().isVisible()));
                result = addConsumer(fields, EVENT.PRIVATE, result, t -> t.getDto().setPrivate(t.getEvent().isPrivate()));

                return result;
            }
            return null;
        }

        private static Consumer<Transformer> addConsumer(final String fields, final String field,
                                                         final Consumer<Transformer> result, final Consumer<Transformer> consumer) {
            if(fields.matches("(.*[,\\s]+?|^)" + field +"([,\\s].*|$)")) {
                return result == null ? consumer : result.andThen(consumer);
            }
            return result;
        }
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
    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
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
    public Boolean getVisible() {
        return visible;
    }
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    public Boolean getPrivate() {
        return isPrivate;
    }
    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventInfo eventInfo = (EventInfo) o;

        if (id != null ? !id.equals(eventInfo.id) : eventInfo.id != null) return false;
        if (name != null ? !name.equals(eventInfo.name) : eventInfo.name != null) return false;
        if (description != null ? !description.equals(eventInfo.description) : eventInfo.description != null)
            return false;
        if (date != null ? !date.equals(eventInfo.date) : eventInfo.date != null) return false;
        if (expireDate != null ? !expireDate.equals(eventInfo.expireDate) : eventInfo.expireDate != null) return false;
        if (typeId != null ? !typeId.equals(eventInfo.typeId) : eventInfo.typeId != null) return false;
        if (location != null ? !location.equals(eventInfo.location) : eventInfo.location != null) return false;
        if (geo != null ? !geo.equals(eventInfo.geo) : eventInfo.geo != null) return false;
        if (visible != null ? !visible.equals(eventInfo.visible) : eventInfo.visible != null) return false;
        return isPrivate != null ? isPrivate.equals(eventInfo.isPrivate) : eventInfo.isPrivate == null;

    }
}
