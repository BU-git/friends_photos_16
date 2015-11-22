package com.bionic.fp.domain;

import com.bionic.fp.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@NamedEntityGraphs({
        @NamedEntityGraph(name="Event.owner", attributeNodes={
                @NamedAttributeNode("owner")}
        ),
        @NamedEntityGraph(name="Event.accounts", attributeNodes={
                @NamedAttributeNode("accounts")}
        ),
        @NamedEntityGraph(name="Event.owner&accounts", attributeNodes={
                @NamedAttributeNode("owner"),
                @NamedAttributeNode("accounts")}
        )
})
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(name = "event_type", nullable = false)
    private EventType eventType;
    @OneToOne(fetch = FetchType.LAZY)
    private Account owner;
    /**
     * Is this event visible in the general mode of search?
     */
    private boolean visible = true;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime date;
    @Column(name = "expire_date")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime expireDate;
    private Double latitude;
    private Double longitude;
    private Float radius;
    @Column(name = "geolocation")
    private boolean geolocationServicesEnabled = false;
    /**
     * Is this event deleted? Because the event is not deleted physically
     */
    private boolean deleted = false;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountEvent> accounts = new ArrayList<>();
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Event() {
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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    public boolean isGeolocationServicesEnabled() {
        return geolocationServicesEnabled;
    }

    public void setGeolocationServicesEnabled(boolean geolocationServicesEnabled) {
        this.geolocationServicesEnabled = geolocationServicesEnabled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<AccountEvent> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEvent> accounts) {
        this.accounts = accounts;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", eventType=").append(eventType);
        sb.append(", owner=").append(owner);
        sb.append(", visible=").append(visible);
        sb.append(", date=").append(date);
        sb.append(", expireDate=").append(expireDate);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", radius=").append(radius);
        sb.append(", geolocationServicesEnabled=").append(geolocationServicesEnabled);
        sb.append(", deleted=").append(deleted);
        sb.append('}');
        return sb.toString();
    }
}