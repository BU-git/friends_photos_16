package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Event;
import com.bionic.fp.util.LocalDateTimeJsonDeserializer;
import com.bionic.fp.util.LocalDateTimeJsonSerializer;
import com.bionic.fp.web.rest.RestConstants.EVENT;
import com.bionic.fp.web.rest.RestConstants.PARAM;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.bionic.fp.util.Checks.check;
import static java.util.Arrays.asList;

/**
 * Holds event info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventInfoDTO {

    @JsonProperty(EVENT.ID)                 private Long id;
    @JsonProperty(EVENT.NAME)               private String name;
    @JsonProperty(EVENT.DESCRIPTION)        private String description;
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonProperty(EVENT.DATE)               private LocalDateTime date;
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonProperty(EVENT.EXPIRE_DATE)        private LocalDateTime expireDate;
    @JsonProperty(EVENT.TYPE_ID)            private Integer typeId;
    @JsonProperty(EVENT.LATITUDE)           private Double latitude;
    @JsonProperty(EVENT.LONGITUDE)          private Double longitude;
    @JsonProperty(EVENT.RADIUS)             private Float radius;
    @JsonProperty(EVENT.GEO)                private Boolean geo;
    @JsonProperty(EVENT.VISIBLE)            private Boolean visible;
    @JsonProperty(EVENT.PRIVATE)            private Boolean isPrivate;

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
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.radius = event.getRadius();
        this.geo = event.isGeoServicesEnabled();
        this.visible = event.isVisible();
        this.isPrivate = event.isPrivate();
    }

    public static class Transformer {
        private final Event event;
        private final EventInfoDTO dto;

        public Transformer(final Event event, final EventInfoDTO dto) {
            check(event != null, "The event is not initialized");
            check(dto != null, "The event dto is not initialized");
            this.event = event;
            this.dto = dto;
        }

        public Event getEvent() {
            return this.event;
        }

        public EventInfoDTO getDto() {
            return this.dto;
        }

        public static EventInfoDTO transform(final Event event, final String fields) {
            if(event == null) {
                return null;
            }
            EventInfoDTO dto = new EventInfoDTO();
            Consumer<Transformer> consumer = getConsumer(fields);
            if(consumer != null) {
                consumer.accept(new Transformer(event, dto));
                return dto;
            }
            dto.build(event);
            return dto;
        }

        public static List<EventInfoDTO> transform(final List<Event> events, final String fields) {
            if(events == null || events.isEmpty()) {
                return Collections.emptyList();
            }
            Consumer<Transformer> consumer = getConsumer(fields);
            if(consumer != null) {
                return events.stream().parallel()
                        .map(event -> {
                            EventInfoDTO dto = new EventInfoDTO();
                            consumer.accept(new Transformer(event, dto));
                            return dto;
                        })
                        .collect(Collectors.toList());
            }
            return events.stream().parallel()
//                    .map(EventInfoDTO::new)
                    .map(e -> {
                        EventInfoDTO dto = new EventInfoDTO();
                        dto.setId(e.getId());
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        public static Consumer<Transformer> getConsumer(final String fields) {
            if(StringUtils.isNotEmpty(fields)) {
                Consumer<Transformer> result = addConsumer(fields, EVENT.ID, null, t -> t.getDto().setId(t.getEvent().getId()));
                result = addConsumer(fields, EVENT.NAME, result, t -> t.getDto().setName(t.getEvent().getName()));
                result = addConsumer(fields, EVENT.DESCRIPTION, result, t -> t.getDto().setDescription(t.getEvent().getDescription()));
                result = addConsumer(fields, EVENT.DATE, result, t -> t.getDto().setDate(t.getEvent().getDate()));
                result = addConsumer(fields, EVENT.EXPIRE_DATE, result, t -> t.getDto().setExpireDate(t.getEvent().getExpireDate()));
                result = addConsumer(fields, EVENT.TYPE_ID, result, t -> t.getDto().setTypeId(t.getEvent().getEventType().getId()));
                result = addConsumer(fields, EVENT.LATITUDE, result, t -> t.getDto().setLatitude(t.getEvent().getLatitude()));
                result = addConsumer(fields, EVENT.LONGITUDE, result, t -> t.getDto().setLongitude(t.getEvent().getLongitude()));
                result = addConsumer(fields, EVENT.RADIUS, result, t -> t.getDto().setRadius(t.getEvent().getRadius()));
                result = addConsumer(fields, EVENT.GEO, result, t -> t.getDto().setGeo(t.getEvent().isGeoServicesEnabled()));
                result = addConsumer(fields, EVENT.VISIBLE, result, t -> t.getDto().setVisible(t.getEvent().isVisible()));
                result = addConsumer(fields, EVENT.PRIVATE, result, t -> t.getDto().setIsPrivate(t.getEvent().isPrivate()));

                return result;
            }
            return null;
        }

        public static Consumer<Transformer> getConsumer(final String... fields) {
            if(ArrayUtils.isNotEmpty(fields)) {
                List<String> list = asList(fields);

                Consumer<Transformer> result = addConsumer(list, EVENT.ID, null, t -> t.getDto().setId(t.getEvent().getId()));
                result = addConsumer(list, EVENT.NAME, result, t -> t.getDto().setName(t.getEvent().getName()));
                result = addConsumer(list, EVENT.DESCRIPTION, result, t -> t.getDto().setDescription(t.getEvent().getDescription()));
                result = addConsumer(list, EVENT.DATE, result, t -> t.getDto().setDate(t.getEvent().getDate()));
                result = addConsumer(list, EVENT.EXPIRE_DATE, result, t -> t.getDto().setExpireDate(t.getEvent().getExpireDate()));
                result = addConsumer(list, EVENT.TYPE_ID, result, t -> t.getDto().setTypeId(t.getEvent().getEventType().getId()));
                result = addConsumer(list, EVENT.LATITUDE, result, t -> t.getDto().setLatitude(t.getEvent().getLatitude()));
                result = addConsumer(list, EVENT.LONGITUDE, result, t -> t.getDto().setLongitude(t.getEvent().getLongitude()));
                result = addConsumer(list, EVENT.RADIUS, result, t -> t.getDto().setRadius(t.getEvent().getRadius()));
                result = addConsumer(list, EVENT.GEO, result, t -> t.getDto().setGeo(t.getEvent().isGeoServicesEnabled()));
                result = addConsumer(list, EVENT.VISIBLE, result, t -> t.getDto().setVisible(t.getEvent().isVisible()));
                result = addConsumer(list, EVENT.PRIVATE, result, t -> t.getDto().setIsPrivate(t.getEvent().isPrivate()));

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

        private static Consumer<Transformer> addConsumer(final List<String> fields, final String field,
                                                         final Consumer<Transformer> result, final Consumer<Transformer> consumer) {
            if(fields.stream().parallel().filter(s -> s.equalsIgnoreCase(field)).findAny().isPresent()) {
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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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
